package org.cucina.audit;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.service.ContextService;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.security.ContextUserAccessor;
import org.eclipse.persistence.history.HistoryPolicy;
import org.eclipse.persistence.internal.helper.ClassConstants;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.ObjectLevelModifyQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;


/**
 * HistoryPolicy which allows for adding of the current user into the historic
 * table so that it's possible to identify who made the change.
 *
 * Specific for EclipseLink.
 *
 */
public class CucinaHistoryPolicy
    extends HistoryPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(CucinaHistoryPolicy.class);
    private static final long serialVersionUID = -6303394509684279225L;
    private static final String AUDIT_TIME = "audit-time";

    /** system */
    public static final String SYSTEM_USERNAME = "system";

    /** This is a field JAVADOC */
    public static final String HISTORY_TABLE_PREFIX = "T_";

    /** This is a field JAVADOC */
    public static final String AUDIT_USER_FIELD = "INF_AUDIT_USER";

    /** This is a field JAVADOC */
    public static final String INF_END_DATE = "INF_END_DATE";

    /** This is a field JAVADOC */
    public static final String INF_START_DATE = "INF_START_DATE";
    private transient ContextService contextService;
    private DatabaseField auditUserField;

    /**
     * Convenience constructor which sets up the HistoryPolicy with the default
     * start, end and auditUser fields. It also sets shouldUseDatabaseTime to
     * true as we always want to use the db time.
     */
    public CucinaHistoryPolicy() {
        super();
        addStartFieldName(INF_START_DATE);
        addEndFieldName(INF_END_DATE);
        auditUserField = new DatabaseField(AUDIT_USER_FIELD);
        auditUserField.setType(ClassConstants.STRING);
        this.setShouldUseDatabaseTime(true);
    }

    /**
     * Convenience constructor which sets up the HistoryPolicy with the provided
     * tableName prefixed with the HISTORY_TABLE_PREFIX
     *
     * @param tableName
     */
    public CucinaHistoryPolicy(String tableName) {
        this();
        Assert.hasText(tableName, "tableName is required!");
        addHistoryTableName(HISTORY_TABLE_PREFIX + tableName);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public DatabaseField getAuditUserField() {
        return auditUserField;
    }

    /**
     * We only want to get the server time once per thread/transaction.
     */
    @Override
    public Object getCurrentTime(AbstractSession session) {
        Object time = getContextService().get(AUDIT_TIME);

        if (time == null) {
            TransactionSynchronizationManager.registerSynchronization(new CleanupTransactionSynchronization());
            time = super.getCurrentTime(session);
            getContextService().put(AUDIT_TIME, time);
        }

        return time;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param session
     *            JAVADOC.
     */
    public void initialize(AbstractSession session) {
        super.initialize(session);
        auditUserField.setTable(getHistoricalTables().get(0));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param writeQuery
     *            JAVADOC.
     * @param isUpdate
     *            JAVADOC.
     */
    @Override
    public void logicalInsert(ObjectLevelModifyQuery writeQuery, boolean isUpdate) {
        AbstractRecord originalModifyRow = writeQuery.getModifyRow();

        if (!isUpdate || checkWastedVersioning(originalModifyRow, getHistoricalTables().get(0))) {
            addUserInfo(originalModifyRow);
        }

        super.logicalInsert(writeQuery, isUpdate);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param originalQuery
     *            JAVADOC.
     * @param arguments
     *            JAVADOC.
     * @param session
     *            JAVADOC.
     */
    @Override
    public void mappingLogicalInsert(DataModifyQuery originalQuery, AbstractRecord arguments,
        AbstractSession session) {
        addUserInfo(originalQuery.getModifyRow());
        addUserInfo(arguments);
        super.mappingLogicalInsert(originalQuery, arguments, session);
    }

    private ContextService getContextService() {
        if (contextService == null) {
            contextService = (ContextService) SingletonBeanFactory.getInstance()
                                                                  .getBean(SingletonBeanFactory.CONTEXT_SERVICE_ID);
        }

        return contextService;
    }

    /**
     * Add the user id to the modified row so that it is inserted along with the
     * copy of the row update/insert.
     */
    private void addUserInfo(AbstractRecord originalModifyRow) {
        if (!originalModifyRow.containsKey(getAuditUserField())) {
            String username = ContextUserAccessor.getCurrentUserName();

            if (StringUtils.isEmpty(username)) {
                username = SYSTEM_USERNAME;
            }

            originalModifyRow.add(getAuditUserField(), username);
        }
    }

    private final class CleanupTransactionSynchronization
        extends TransactionSynchronizationAdapter {
        @Override
        public void afterCompletion(int status) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("transaction complete - removing " + AUDIT_TIME +
                    " from properties service");
            }

            CucinaHistoryPolicy.this.getContextService().put(AUDIT_TIME, null);
        }
    }
}
