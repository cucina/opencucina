package org.cucina.engine.server.utils;

import org.apache.commons.lang3.StringUtils;
import org.cucina.conversation.Operative;
import org.cucina.conversation.events.CallbackEvent;
import org.cucina.conversation.events.ConversationEvent;
import org.cucina.conversation.events.RollbackEvent;
import org.cucina.core.service.ContextService;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.LocalProcessDriver;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.definition.Check;
import org.cucina.engine.definition.Operation;
import org.cucina.engine.server.definition.AbstractElementDescriptor;
import org.cucina.engine.server.definition.CheckDescriptor;
import org.cucina.engine.server.definition.OperationDescriptor;
import org.cucina.engine.server.definition.ProcessElementDto;
import org.cucina.engine.server.event.BooleanEvent;
import org.cucina.engine.server.model.EntityDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Persistable;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component("processDriver")
public class MessagingProcessDriver
		extends MessagingGatewaySupport
		implements ProcessDriver {
	private static final Logger LOG = LoggerFactory.getLogger(MessagingProcessDriver.class);
	private ContextService contextService;
	private ConversionService conversionService;
	private ProcessDriver localDriver;
	private RegistryUrlExtractor registryUrlExtractor;

	/**
	 * Creates a new MessagingProcessDriver object.
	 *
	 * @param conversionService .
	 */
	@Autowired
	public MessagingProcessDriver(
			@Qualifier("myConversionService")
					ConversionService conversionService, ContextService contextService,
			RegistryUrlExtractor registryUrlExtractor) {
		Assert.notNull(conversionService, "conversionService is null");
		this.conversionService = conversionService;
		Assert.notNull(contextService, "contextService is null");
		this.contextService = contextService;
		Assert.notNull(registryUrlExtractor, "registryUrlExtractor is null");
		this.registryUrlExtractor = registryUrlExtractor;
	}

	/**
	 * @param localDriver .
	 */
	public void setLocalDriver(ProcessDriver localDriver) {
		this.localDriver = localDriver;
	}

	/**
	 * @param conversationReply .
	 */
	@Autowired
	@Override
	public void setReplyChannel(@Qualifier("conversationReply")
										MessageChannel conversationReply) {
		super.setReplyChannel(conversationReply);
	}

	/**
	 * @param conversation .
	 */
	@Autowired
	@Override
	public void setRequestChannel(@Qualifier("conversation")
										  MessageChannel conversation) {
		super.setRequestChannel(conversation);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param actions          JAVADOC.
	 * @param executionContext JAVADOC.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void execute(List<Operation> actions, ExecutionContext executionContext) {
		for (Operation action : actions) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Action:" + action);
			}

			if (action instanceof OperationDescriptor) {
				OperationDescriptor aed = (OperationDescriptor) action;
				Object result = process(aed, executionContext);

				LOG.debug("Action result is :'" + result + "'");

				if (result instanceof Map<?, ?>) {
					executionContext.getParameters()
							.putAll((Map<? extends String, ? extends Object>) result);

					continue;
				}
			} else {
				// this should not happen with workflow-rules-definition.xml
				// specifying ActionDescriptor
				LOG.warn("Action '" + action + "' is not WorkflowElementDescriptor");
			}
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param condition        JAVADOC.
	 * @param executionContext JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean test(Check condition, ExecutionContext executionContext) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Condition:" + condition);
		}

		if (condition instanceof CheckDescriptor) {
			CheckDescriptor ced = (CheckDescriptor) condition;

			Object result = process(ced, executionContext);

			LOG.debug("Remote condition returned result:'" + result + "'");

			if (result instanceof BooleanEvent) {
				return ((BooleanEvent) result).isResult();
			}

			ConversationEvent engineEvent = (ConversationEvent) result;

			if (engineEvent instanceof RollbackEvent) {
				throw new RuntimeException("Rollback");
			}
		} else {
			// this should not happen with workflow-rules-definition.xml
			// specifying ConditionDescriptor
			LOG.warn("Condition is not a descriptor");
		}

		return false;
	}

	/**
	 * @throws Exception .
	 */
	@Override
	protected void onInit()
			throws Exception {
		if (localDriver == null) {
			localDriver = new LocalProcessDriver();
		}
	}

	private Object process(AbstractElementDescriptor descriptor, ExecutionContext context) {
		String application = (String) descriptor.get("application");
		BeanWrapper beanWrapper = new BeanWrapperImpl(context.getToken().getDomainObject());

		descriptor.put("domainType", beanWrapper.getPropertyValue("applicationType"));

		Persistable<?> po = context.getToken().getDomainObject();

		descriptor.put("domainId",
				(po instanceof EntityDescriptor) ? ((EntityDescriptor) po).getRemoteId() : po.getId());

		if (StringUtils.isEmpty(application)) {
			throw new IllegalArgumentException("application is not specified");

			// return testLocal(ced, executionContext);
		}

		return sendAndReceive(conversionService.convert(descriptor, ProcessElementDto.class),
				context);
	}

	private Object sendAndReceive(ProcessElementDto wed, ExecutionContext ec) {
		CallbackEvent event = new CallbackEvent(wed, ec.getParameters(), wed.getApplication());
		Message<CallbackEvent> message = MessageBuilder.withPayload(event)
				.setHeader(Operative.CONVERSATION_ID,
						contextService.get(Operative.CONVERSATION_ID))
				.setHeader(Operative.DESTINATION_NAME,
						registryUrlExtractor.findDestinationName(wed.getApplication())).build();

		return sendAndReceive(message);
	}
}
