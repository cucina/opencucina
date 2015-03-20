package org.cucina.engine.server.utils;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.cucina.engine.definition.Token;
import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.event.CommitEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.GetValueEvent;
import org.cucina.engine.server.event.RollbackEvent;
import org.cucina.engine.server.event.workflow.BulkTransitionEvent;
import org.cucina.engine.server.event.workflow.ListActionableTransitionsEvent;
import org.cucina.engine.server.event.workflow.ListAllTransitionsEvent;
import org.cucina.engine.server.event.workflow.ListTransitionsEvent;
import org.cucina.engine.server.event.workflow.ListWorkflowPropertiesEvent;
import org.cucina.engine.server.event.workflow.LoadTransitionInfoEvent;
import org.cucina.engine.server.event.workflow.ObtainHistoryEvent;
import org.cucina.engine.server.event.workflow.ObtainHistorySummaryEvent;
import org.cucina.engine.server.event.workflow.SingleTransitionEvent;
import org.cucina.engine.server.event.workflow.StartWorkflowEvent;
import org.cucina.engine.server.event.workflow.ValueEvent;
import org.cucina.engine.server.event.workflow.WorkflowEvent;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.service.ProcessSupportService;

import org.cucina.i18n.api.ListNodeDto;
import org.cucina.i18n.api.ListNodeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Component
public class MessagingWorkflowServiceHandlerImpl
    implements MessagingWorkflowServiceHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MessagingWorkflowServiceHandlerImpl.class);
    private ConversationContext conversationContext;
    private ListNodeService listNodeService;
    private ProcessSupportService workflowSupportService;

    /**
     * Creates a new MessagingWorkflowServiceHandlerImpl object.
     *
     * @param conversationContext JAVADOC.
     * @param listNodeDao JAVADOC.
     * @param workflowSupportService JAVADOC.
     */
    @Autowired
    public MessagingWorkflowServiceHandlerImpl(ConversationContext conversationContext,
        ListNodeService listNodeService, ProcessSupportService workflowSupportService) {
        Assert.notNull(conversationContext, "conversationContext is null");
        this.conversationContext = conversationContext;
        Assert.notNull(listNodeService, "listNodeService is null");
        this.listNodeService = listNodeService;
        Assert.notNull(workflowSupportService, "workflowSupportService is null");
        this.workflowSupportService = workflowSupportService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public ValueEvent getValue(GetValueEvent event) {
        Object result = null;

        if (event instanceof ListActionableTransitionsEvent) {
            result = workflowSupportService.listActionableTransitions(((ListActionableTransitionsEvent) event).getWorkflowId());
        } else if (event instanceof ListAllTransitionsEvent) {
            ListAllTransitionsEvent lae = (ListAllTransitionsEvent) event;

            result = workflowSupportService.listAllTransitions(lae.getIds(),
                    lae.getApplicationType());
        } else if (event instanceof ListTransitionsEvent) {
            ListTransitionsEvent lae = (ListTransitionsEvent) event;

            workflowSupportService.listTransitions(lae.getIds(), lae.getApplicationType());
        } else if (event instanceof ListWorkflowPropertiesEvent) {
            ListWorkflowPropertiesEvent lae = (ListWorkflowPropertiesEvent) event;

            workflowSupportService.listWorkflowProperties(lae.getIds(), lae.getApplicationType());
        } else if (event instanceof LoadTransitionInfoEvent) {
            result = workflowSupportService.loadTransitionInfo(((LoadTransitionInfoEvent) event).getWorkflowId());
        } else if (event instanceof ObtainHistoryEvent) {
            ObtainHistoryEvent lae = (ObtainHistoryEvent) event;

            workflowSupportService.obtainHistory(lae.getId(), lae.getApplicationType());
        } else if (event instanceof ObtainHistorySummaryEvent) {
            ObtainHistorySummaryEvent lae = (ObtainHistorySummaryEvent) event;

            result = workflowSupportService.obtainHistorySummary(lae.getId(),
                    lae.getApplicationType());
        } else {
            throw new IllegalArgumentException("Cannot handle event " + event);
        }

        ValueEvent valueEvent = new ValueEvent(event);

        valueEvent.setValue(result);

        return valueEvent;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public EngineEvent act(Message<WorkflowEvent> message) {
        String conversationId = (String) message.getHeaders()
                                                .get(ConversationContext.CONVERSATION_ID);

        if (StringUtils.isNotEmpty(conversationId)) {
            conversationContext.setConversationId(conversationId);
        }

        WorkflowEvent event = message.getPayload();

        try {
            if (event instanceof StartWorkflowEvent) {
                StartWorkflowEvent we = (StartWorkflowEvent) event;

                Token token = workflowSupportService.startWorkflow(new EntityDescriptor(
                            we.getType(), (Long) we.getId(), we.getApplicationName()),
                        we.getParameters());

                if (token == null) {
                    return new RollbackEvent("Failed to start workflow");
                }
            } else if (event instanceof SingleTransitionEvent) {
                SingleTransitionEvent we = (SingleTransitionEvent) event;

                workflowSupportService.makeTransition((Long) we.getId(), we.getType(),
                    we.getTransitionId(), we.getComment(), we.getApprovedAs(), we.getAssignedTo(),
                    we.getExtraParams(), we.getAttachment());
            } else if (event instanceof BulkTransitionEvent) {
                BulkTransitionEvent we = (BulkTransitionEvent) event;
                Collection<ListNodeDto> reasons = listNodeService.loadByType(we.getReason());

                if (CollectionUtils.isEmpty(reasons)) {
                    LOG.warn("Failed to load reason for string '" + we.getReason() + "'");

                    return new RollbackEvent("Failed to load reason for string '" + we.getReason() +
                        "'");
                }

                workflowSupportService.makeTransition(we.getEntities(), we.getType(),
                    we.getTransitionId(), we.getComment(), we.getApprovedAs(), we.getAssignedTo(),
                    we.getExtraParams(), reasons.iterator().next(), we.getAttachment());
            }

            return new CommitEvent(event);
        } catch (Exception e) {
            LOG.error("Oops", e);

            return new RollbackEvent(event);
        }
    }
}
