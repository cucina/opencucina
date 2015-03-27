package org.engine.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.security.core.token.Token;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.communication.HistoryRecordDto;
import org.cucina.engine.server.event.CommitSuccessEvent;
import org.cucina.engine.server.event.CompensateEvent;
import org.cucina.engine.server.event.RegistrationEvent;
import org.cucina.engine.server.event.RollbackEvent;
import org.cucina.engine.server.event.workflow.BulkTransitionEvent;
import org.cucina.engine.server.event.workflow.ListTransitionsEvent;
import org.cucina.engine.server.event.workflow.ListWorkflowPropertiesEvent;
import org.cucina.engine.server.event.workflow.LoadTransitionInfoEvent;
import org.cucina.engine.server.event.workflow.ObtainHistoryEvent;
import org.cucina.engine.server.event.workflow.ObtainHistorySummaryEvent;
import org.cucina.engine.server.event.workflow.SingleTransitionEvent;
import org.cucina.engine.server.event.workflow.StartWorkflowEvent;
import org.cucina.engine.server.event.workflow.ValueEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MessagingProcessEngineFacade
    extends MessagingGatewaySupport
    implements ProcessEngineFacade, ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(MessagingProcessEngineFacade.class);
    private MessageChannel asyncChannel;
    private String applicationName;
    private String myQueue;

    /**
     * Creates a new MessagingWorkflowSupportService object.
     *
     * @param myQueue
     *            JAVADOC.
     */
    public MessagingProcessEngineFacade(String applicationName, String myQueue,
        MessageChannel asyncChannel) {
        this.myQueue = myQueue;
        this.applicationName = applicationName;
        Assert.notNull(asyncChannel, "asyncChannel is null");
        this.asyncChannel = asyncChannel;
    }

    /**
     * Bulk transitions list. Server will assure that all objects are in the
     * same place.
     *
     * @param ids
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<String> listTransitions(Collection<Long> ids, String applicationType) {
        ListTransitionsEvent event = new ListTransitionsEvent(applicationType, applicationName);

        event.setApplicationType(applicationType);
        event.setIds(ids);

        return sendForValue(event);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param ids
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Map<String, Object>> listWorkflowProperties(Collection<Long> ids,
        String applicationType) {
        ListWorkflowPropertiesEvent event = new ListWorkflowPropertiesEvent(applicationType,
                applicationName);

        event.setApplicationType(applicationType);
        event.setIds(ids);

        return sendForValue(event);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowId
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Map<String, String>> loadTransitionInfo(String workflowId) {
        LoadTransitionInfoEvent event = new LoadTransitionInfoEvent(applicationName, applicationName);

        event.setWorkflowId(workflowId);

        return sendForValue(event);
    }

    /**
     * Bulk transition
     *
     * @param entities
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     * @param transitionId
     *            JAVADOC.
     * @param comment
     *            JAVADOC.
     * @param approvedAs
     *            JAVADOC.
     * @param assignedTo
     *            JAVADOC.
     * @param extraParams
     *            JAVADOC.
     * @param reason
     *            JAVADOC.
     * @param attachment
     *            JAVADOC.
     */
    @Override
    public void bulkTransition(Map<Long, Integer> entities, String applicationType,
        String transitionId, String comment, String approvedAs, String assignedTo,
        Map<String, Object> extraParams, String reason, Object attachment) {
        BulkTransitionEvent event = new BulkTransitionEvent(applicationType, applicationName);

        event.setType(applicationType);
        event.setEntities(entities);
        event.setTransitionId(transitionId);
        event.setComment(comment);
        event.setApprovedAs(approvedAs);
        event.setAssignedTo(assignedTo);
        event.setExtraParams(extraParams);
        event.setAttachment(attachment);

        Object reply = sendForReply(event);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Reply:" + reply);
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     * @param transitionId
     *            JAVADOC.
     * @param comment
     *            JAVADOC.
     * @param approvedAs
     *            JAVADOC.
     * @param assignedTo
     *            JAVADOC.
     * @param extraParams
     *            JAVADOC.
     * @param attachment
     *            JAVADOC.
     */
    @Override
    public void makeTransition(String applicationType, Long id, String transitionId,
        String comment, String approvedAs, String assignedTo, Map<String, Object> extraParams,
        Object attachment) {
        SingleTransitionEvent event = new SingleTransitionEvent(applicationType, applicationName);

        event.setType(applicationType);
        event.setId(id);
        event.setTransitionId(transitionId);
        event.setComment(comment);
        event.setApprovedAs(approvedAs);
        event.setAssignedTo(assignedTo);
        event.setExtraParams(extraParams);
        event.setAttachment(attachment);

        Object reply = sendForReply(event);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Reply:" + reply);
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public List<HistoryRecordDto> obtainHistory(Long id, String applicationType) {
        ObtainHistoryEvent event = new ObtainHistoryEvent(applicationType, applicationName);

        event.setApplicationType(applicationType);
        event.setId(id);

        return sendForValue(event);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     * @param applicationType
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public List<Map<String, Object>> obtainHistorySummary(Long id, String applicationType) {
        ObtainHistorySummaryEvent event = new ObtainHistorySummaryEvent(applicationType,
                applicationName);

        event.setApplicationType(applicationType);
        event.setId(id);

        return sendForValue(event);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        Message<?> message = MessageBuilder.withPayload(new RegistrationEvent(applicationName,
                    applicationName, "jms://" + myQueue)).build();

        asyncChannel.send(message);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param entity
     *            JAVADOC.
     * @param parameters
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Token startWorkflow(String entityType, Long id, Map<String, Object> parameters) {
        return startWorkflow(entityType, id, entityType, parameters);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param entity
     *            JAVADOC.
     * @param workflowId
     *            JAVADOC.
     * @param parameters
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Token startWorkflow(final String entityType, final Long id, String workflowId,
        Map<String, Object> parameters) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCompletion(int status) {
                        handleStatus(status, entityType, id);
                    }
                });
        }

        StartWorkflowEvent event = new StartWorkflowEvent(entityType, applicationName);

        event.setType(entityType);
        event.setId(id);
        event.setParameters(parameters);

        Object reply = sendForReply(event);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Reply:" + reply);
        }

        return null;
    }

    private Message<?> buildMessage(Object payload) {
        String conversationId = UUID.randomUUID().toString();

        return MessageBuilder.withPayload(payload)
                             .setHeader(ConversationContext.CONVERSATION_ID, conversationId).build();
    }

    private void handleStatus(int status, String type, Long... ids) {
        Message<?> callmess;

        if (TransactionSynchronization.STATUS_COMMITTED == status) {
            // TODO make CommitSuccess handle type/ids combo
            callmess = MessageBuilder.withPayload(new CommitSuccessEvent(ids)).build();
        } else {
            CompensateEvent compensateEvent = new CompensateEvent(status);

            compensateEvent.setIds(ids);
            compensateEvent.setType(type);
            callmess = MessageBuilder.withPayload(compensateEvent).build();
            LOG.debug("Compensating for " + type + ":" + Arrays.toString(ids));
        }

        asyncChannel.send(callmess);
    }

    @SuppressWarnings("unchecked")
    private <T> T sendForReply(Object obj) {
        Message<?> reply = sendAndReceiveMessage(buildMessage(obj));

        if (LOG.isDebugEnabled()) {
            LOG.debug("Received " + reply);
        }

        if (reply instanceof ErrorMessage) {
            LOG.error("Oops", ((ErrorMessage) reply).getPayload());

            return null;
        }

        if (reply instanceof RollbackEvent) {
            throw new RuntimeException("Rollback on the server");
        }

        return (T) reply.getPayload();
    }

    @SuppressWarnings("unchecked")
    private <T> T sendForValue(Object obj) {
        Object result = sendForReply(obj);

        if (result instanceof ValueEvent) {
            return (T) ((ValueEvent) result).getValue();
        }

        LOG.error("Cannot process payload " + result);

        return null;
    }
}
