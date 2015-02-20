package org.cucina.audit;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Message;
import org.cucina.core.model.PersistableEntity;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.history.AsOfClause;
import org.eclipse.persistence.history.HistoryPolicy;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ClientSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of <code>AuditService</code> which makes use of eclipselink
 * <code>HistoryPolicy</code> to support auditing.

 *
 */
public class AuditServiceImpl
    implements AuditService {
    /** This is a field JAVADOC */
    public static final String AS_OF_PATTERN = "yyyy/MM/dd HH:mm:ss.SSS";
    private static final Logger LOG = LoggerFactory.getLogger(AuditServiceImpl.class);
    private InstanceFactory instanceFactory;
    private JdbcTemplate jdbcTemplate;

    /**
    * Used JpaEntityManager instead of the EntityManager interface because that's
    * what we need here and it cannot be cast later because it will be a proxy created
    * by spring in the app.
    */
    @PersistenceContext
    private JpaEntityManager entityManager;

    /**
     * JAVADOC Method Level Comments
     *
     * @param entityManager JAVADOC.
     */
    public void setEntityManager(JpaEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param instanceFactory JAVADOC.
     */
    @Required
    public void setInstanceFactory(InstanceFactory instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param jdbcTemplate JAVADOC.
     */
    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public List<AuditRecord> listAuditRecords(String applicationType, Long id) {
        List<AuditRecord> auditRecords = new ArrayList<AuditRecord>();
        Class<?> clazz = instanceFactory.getClassType(applicationType);

        if (!PersistableEntity.class.isAssignableFrom(clazz)) {
            LOG.debug("Cannot determine history for " + applicationType +
                " as it's not a PersistableEntity");
        }

        Project project = entityManager.getDatabaseSession().getProject();
        ClassDescriptor descriptor = project.getDescriptor(clazz);
        HistoryPolicy historyPolicy = descriptor.getHistoryPolicy();

        if (historyPolicy != null) {
            String histTableName = historyPolicy.getHistoryTableNames().get(0);
            String startFieldName = historyPolicy.getStartFieldName();
            String endFieldName = historyPolicy.getEndFieldName();
            List<Object> params = new ArrayList<Object>();
            StringBuilder query = null;
            StringBuilder domainQuery = new StringBuilder("select ").append(StringUtils.join(
                        Arrays.asList(startFieldName, endFieldName,
                            CucinaHistoryPolicy.AUDIT_USER_FIELD), ",")).append(" from ")
                                                                    .append(histTableName)
                                                                    .append(" where ")
                                                                    .append(PersistableEntity.ID_PROPERTY)
                                                                    .append(" = ?");

            params.add(id);
            query = domainQuery;
            query.append(" order by ").append(startFieldName).append(" desc");

            List<Map<String, Object>> results = jdbcTemplate.queryForList(query.toString(),
                    params.toArray());

            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> row = results.get(i);
                AuditRecord ar = new AuditRecord();

                ar.setModifiedDate((Timestamp) row.get(startFieldName));
                ar.setModifiedBy((String) row.get(CucinaHistoryPolicy.AUDIT_USER_FIELD));
                auditRecords.add(ar);

                if ((i == 0) && (row.get(endFieldName) != null)) {
                    ar.setAction(AuditRecord.Operation.DELETE.toString());
                } else if (i == (results.size() - 1)) {
                    ar.setAction(AuditRecord.Operation.INSERT.toString());
                } else {
                    ar.setAction(AuditRecord.Operation.UPDATE.toString());
                }
            }
        }

        return auditRecords;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     * @param id JAVADOC.
     * @param auditDate JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public List<AuditDifference> listChangeDetails(String applicationType, Long id,
        Timestamp auditDate) {
        ClientSession clientSession = entityManager.getServerSession().acquireClientSession();
        Session histSession = clientSession.acquireHistoricalSession(new AsOfClause(auditDate));
        PersistableEntity readObj = instanceFactory.getBean(applicationType);

        readObj.setId(id);

        Object current = histSession.readObject(readObj);

        Date previousDate = new Timestamp(auditDate.getTime() - 1);

        histSession = clientSession.acquireHistoricalSession(new AsOfClause(previousDate));

        if (LOG.isDebugEnabled()) {
            LOG.debug("Querying as of " + auditDate + " for new value");
            LOG.debug("Querying as of " + previousDate + " for old value");
        }

        Object previous = histSession.readObject(readObj);

        if (previous == null) {
            LOG.debug("No earlier record found so no changes available.");

            return Collections.emptyList();
        }

        BeanWrapper cBean = new BeanWrapperImpl(current);
        BeanWrapper pBean = new BeanWrapperImpl(previous);
        List<AuditDifference> changes = new ArrayList<AuditDifference>();

        for (int i = 0; i < cBean.getPropertyDescriptors().length; i++) {
            String propName = cBean.getPropertyDescriptors()[i].getName();

            Object newValue = cBean.getPropertyValue(propName);
            Object oldValue = pBean.getPropertyValue(propName);

            if (LOG.isDebugEnabled()) {
                LOG.debug("comparing prop = " + propName);
                LOG.debug("newValue = " + newValue);
                LOG.debug("oldValue = " + oldValue);
            }

            if (!equal(newValue, oldValue)) {
                LOG.debug("adding AuditDifference for " + propName);
                changes.add(new AuditDifference(propName, newValue, oldValue));
            }
        }

        return changes;
    }

    /**
    * Convenience method to test for equality for the purposes of audit.
    * @param prop1
    * @param prop2
    * @return true if the two objects are equal
    */
    @SuppressWarnings("null")
    private boolean equal(Object prop1, Object prop2) {
        if ((prop1 == null) && (prop2 == null)) {
            return true;
        }

        if (((prop1 == null) && (prop2 != null)) || ((prop1 != null) && (prop2 == null))) {
            return false;
        }

        if (prop1 instanceof Collection<?>) {
            Collection<?> c1 = (Collection<?>) prop1;
            Collection<?> c2 = (Collection<?>) prop2;

            LOG.debug("Collection1 is " + c1.size() + " Collection2 is " + c2.size());

            return CollectionUtils.disjunction(c1, c2).isEmpty();
        }

        if (prop1 instanceof Message) {
            Message m1 = (Message) prop1;
            Message m2 = (Message) prop2;

            return m1.equalsMessages(m2);
        }

        return prop1.equals(prop2);
    }
}
