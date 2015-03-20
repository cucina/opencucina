package org.cucina.engine.listeners;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Attachment;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.Decision;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.ProcessToken;
import org.cucina.security.ContextUserAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class HistoryListener
    extends WorkflowListenerAdapter {
    private static final String STATUS = "status";

    /** This is a field JAVADOC */
    public static final String HISTORY_RECORD = "historyRecord";

    /** attachment */
    public static final String ATTACHMENT_PROPERTY = "attachment";

    /** This is a field JAVADOC */
    public static final String APPROVEDBY_PROPERTY = "approvedBy";

    /** This is a field JAVADOC */
    public static final String ASSIGNEDTO_PROPERTY = "assignedTo";

    /** This is a field JAVADOC */
    public static final String REASON_PROPERTY = "reason";
    private static final Logger LOG = LoggerFactory.getLogger(HistoryListener.class);
    private InstanceFactory instanceFactory;

    /**
     * JAVADOC Method Level Comments
     *
     * @param instanceFactory
     *            JAVADOC.
     */
    public void setInstanceFactory(InstanceFactory instanceFactory) {
        this.instanceFactory = instanceFactory;
    }

    /**
     * Records changes from context into the token histories
     *
     * @param state
     *            JAVADOC.
     * @param from
     *            JAVADOC.
     * @param executionContext
     *            JAVADOC.
     */
    @Override
    public void enteredState(State state, Transition from, ExecutionContext executionContext) {
        HistoryRecord historyRecord = (HistoryRecord) executionContext.getParameters()
                                                                      .get(HISTORY_RECORD);

        if (historyRecord == null) {
            return;
        }

        Map<String, Object> parameters = executionContext.getParameters();
        BeanWrapper beanWrapper = new BeanWrapperImpl(historyRecord);

        for (java.util.Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (!beanWrapper.isWritableProperty(entry.getKey())) {
                LOG.debug("Property '" + entry.getKey() + "' has no setter on object of type:" +
                    historyRecord.getClass().getName());

                continue;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Setting historicalRecord property '" + entry.getKey() + "' to " +
                    entry.getValue());
            }

            beanWrapper.setPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Create new history only if there are changes, i.e. comments in the
     * context parameters.
     *
     * @param state
     *            JAVADOC.
     * @param transition
     *            JAVADOC.
     * @param executionContext
     *            JAVADOC.
     */
    @Override
    public void leavingState(State state, Transition transition, ExecutionContext executionContext) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Leaving state:" + state.getId() + " by transition " + transition.getId());
        }

        ProcessToken token = (ProcessToken) executionContext.getToken();

        Map<String, Object> parameters = executionContext.getParameters();

        boolean changes = parameters.containsKey(HistoryRecord.COMMENTS_PROPERTY);

        // If there are no changes or we are going to enter a Decision, ignore
        if (!changes || transition.getOutput() instanceof Decision) {
            LOG.debug("No changes");

            // clean up just in case
            executionContext.getParameters().remove(HISTORY_RECORD);

            return;
        }

        calculateCarryovers(parameters, token);

        HistoryRecord history = instanceFactory.getBean(HistoryRecord.class.getSimpleName());

        history.setToken(token);
        history.setModifiedBy(ContextUserAccessor.getCurrentUserName());
        history.setModifiedDate(new Date());

        Attachment attachment = (Attachment) parameters.get(ATTACHMENT_PROPERTY);

        if (attachment != null) {
            history.setAttachment(attachment);
            parameters.remove(ATTACHMENT_PROPERTY);
        }

        token.addHistoryRecord(history);

        executionContext.getParameters().put(HISTORY_RECORD, history);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Histories=" + token.getHistories());
        }
    }

    private void calculateCarryovers(Map<String, Object> parameters, ProcessToken token) {
        List<HistoryRecord> histories = token.getHistories();

        HistoryRecord last = null;

        if ((histories != null) && !histories.isEmpty()) {
            last = histories.get(histories.size() - 1);
        }

        if (last == null) {
            LOG.debug("No previous record");

            return;
        }

        carryOverString(last.getStatus(), parameters, STATUS);
        carryOverString(last.getApprovedBy(), parameters, APPROVEDBY_PROPERTY);
    }

    private void carryOverString(String previous, Map<String, Object> parameters, String key) {
        String value = (String) parameters.get(key);

        if (LOG.isDebugEnabled()) {
            LOG.debug("previous:" + previous + " value:" + value);
        }

        if (value == null) {
            value = previous;
        }

        parameters.put(key, value);
    }
}
