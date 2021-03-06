package org.cucina.engine.client;

import org.cucina.conversation.Operative;
import org.cucina.conversation.TransactionHandler;
import org.cucina.conversation.events.CommitEvent;
import org.cucina.conversation.events.RollbackEvent;
import org.cucina.engine.server.definition.HistoryRecordDto;
import org.cucina.engine.server.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MessagingProcessEngineFacade
		extends MessagingGatewaySupport
		implements ProcessEngineFacade {
	private static final Logger LOG = LoggerFactory.getLogger(MessagingProcessEngineFacade.class);
	private String applicationName = "remove applicationName from MessagingProcessEngineFacade";
	@Autowired
	private TransactionHandler transactionHandler;

	/**
	 * Bulk transition
	 *
	 * @param entities        JAVADOC.
	 * @param applicationType JAVADOC.
	 * @param transitionId    JAVADOC.
	 * @param comment         JAVADOC.
	 * @param approvedAs      JAVADOC.
	 * @param assignedTo      JAVADOC.
	 * @param extraParams     JAVADOC.
	 * @param reason          JAVADOC.
	 * @param attachment      JAVADOC.
	 */
	@Override
	public void bulkTransition(Map<Serializable, Integer> entities, String entityType,
							   String transitionId, String comment, String approvedAs, String assignedTo,
							   Map<String, Object> extraParams, String reason, Object attachment) {
		transactionHandler.registerTxHandler(entityType,
				entities.keySet().toArray(new Serializable[entities.size()]));

		BulkTransitionEvent event = new BulkTransitionEvent(entityType, applicationName);

		event.setType(entityType);
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
	 * Bulk transitions list. Server will assure that all objects are in the
	 * same place.
	 *
	 * @param ids             JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Collection<String> listTransitions(Collection<Serializable> ids, String applicationType) {
		ListTransitionsEvent event = new ListTransitionsEvent(applicationType, applicationName);

		event.setApplicationType(applicationType);
		event.setIds(ids);

		return sendForValue(event);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param ids             JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Collection<Map<String, Object>> listWorkflowProperties(Collection<Serializable> ids,
																  String applicationType) {
		ListProcessPropertiesEvent event = new ListProcessPropertiesEvent(applicationType,
				applicationName);

		event.setApplicationType(applicationType);
		event.setIds(ids);

		return sendForValue(event);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param workflowId JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Collection<Map<String, String>> loadTransitionInfo(String workflowId) {
		LoadTransitionInfoEvent event = new LoadTransitionInfoEvent(applicationName, applicationName);

		event.setWorkflowId(workflowId);

		return sendForValue(event);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id           JAVADOC.
	 * @param entityType   JAVADOC.
	 * @param transitionId JAVADOC.
	 * @param comment      JAVADOC.
	 * @param approvedAs   JAVADOC.
	 * @param assignedTo   JAVADOC.
	 * @param extraParams  JAVADOC.
	 * @param attachment   JAVADOC.
	 */
	@Override
	public void makeTransition(String entityType, Serializable id, String transitionId,
							   String comment, String approvedAs, String assignedTo, Map<String, Object> extraParams,
							   Object attachment) {
		transactionHandler.registerTxHandler(entityType, id);

		SingleTransitionEvent event = new SingleTransitionEvent(entityType, applicationName);

		event.setType(entityType);
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
	 * @param id              JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public List<HistoryRecordDto> obtainHistory(Serializable id, String applicationType) {
		ObtainHistoryEvent event = new ObtainHistoryEvent(applicationType, applicationName);

		event.setApplicationType(applicationType);
		event.setId(id);

		return sendForValue(event);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id              JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public List<Map<String, Object>> obtainHistorySummary(Serializable id, String applicationType) {
		ObtainHistorySummaryEvent event = new ObtainHistorySummaryEvent(applicationType,
				applicationName);

		event.setApplicationType(applicationType);
		event.setId(id);

		return sendForValue(event);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param entity     JAVADOC.
	 * @param parameters JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean startWorkflow(String entityType, Serializable id, Map<String, Object> parameters) {
		return startWorkflow(entityType, id, entityType, parameters);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param entity     JAVADOC.
	 * @param workflowId JAVADOC.
	 * @param parameters JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean startWorkflow(String entityType, Serializable id, String workflowId,
								 Map<String, Object> parameters) {
		transactionHandler.registerTxHandler(entityType, id);

		StartWorkflowEvent event = new StartWorkflowEvent(entityType, applicationName);

		event.setType(entityType);
		event.setId(id);
		event.setParameters(parameters);
		event.setWorkflow(workflowId);

		Object reply = sendForReply(event);

		if (reply instanceof CommitEvent) {
			return true;
		}

		// TODO is it a case for rollback if the result is false or not a
		// CommitEvent at all ?
		if (reply instanceof ValueEvent) {
			return (Boolean) ((ValueEvent) reply).getValue();
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private <T> T sendForReply(Object obj) {
		Message<?> reply = sendAndReceiveMessage(MessageBuilder.withPayload(obj)
				.setHeader(Operative.DESTINATION_NAME,
						"server.queue").build());

		if (LOG.isDebugEnabled()) {
			LOG.debug("Received " + reply);
		}

		if (reply instanceof ErrorMessage) {
			LOG.error("Oops", ((ErrorMessage) reply).getPayload());

			return null;
		}

		if (reply.getPayload() instanceof RollbackEvent) {
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
