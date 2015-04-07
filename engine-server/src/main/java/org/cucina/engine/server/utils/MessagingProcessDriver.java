package org.cucina.engine.server.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Persistable;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.util.Assert;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.LocalProcessDriver;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.definition.Check;
import org.cucina.engine.definition.Operation;
import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.definition.AbstractElementDescriptor;
import org.cucina.engine.server.definition.CheckDescriptor;
import org.cucina.engine.server.definition.OperationDescriptor;
import org.cucina.engine.server.definition.WorkflowElementDto;
import org.cucina.engine.server.event.BooleanEvent;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.RollbackEvent;
import org.cucina.engine.server.model.EntityDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MessagingProcessDriver
    extends MessagingGatewaySupport
    implements ProcessDriver {
    private static final Logger LOG = LoggerFactory.getLogger(MessagingProcessDriver.class);
    private ConversationContext conversationContext;
    private ConversionService conversionService;
    private ProcessDriver localDriver;

    /**
    * Creates a new MessagingWorkflowDriver object.
    *
    * @param conversationContext JAVADOC.
    * @param localDriver JAVADOC.
    * @param applicationName JAVADOC.
    */
    public MessagingProcessDriver(ConversationContext conversationContext,
        ConversionService conversionService) {
        Assert.notNull(conversationContext, "conversationContext is null");
        this.conversationContext = conversationContext;
        Assert.notNull(conversionService, "conversionService is null");
        this.conversionService = conversionService;
    }

    /**
     *
     *
     * @param localDriver .
     */
    public void setLocalDriver(ProcessDriver localDriver) {
        this.localDriver = localDriver;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param actions
     *            JAVADOC.
     * @param executionContext
     *            JAVADOC.
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
                                    .putAll((Map<?extends String, ?extends Object>) result);

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
     * @param condition
     *            JAVADOC.
     * @param executionContext
     *            JAVADOC.
     *
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

            EngineEvent engineEvent = (EngineEvent) result;

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
     *
     *
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

            //return testLocal(ced, executionContext);
        }

        return sendAndReceive(conversionService.convert(descriptor, WorkflowElementDto.class),
            context);
    }

    private Object sendAndReceive(WorkflowElementDto wed, ExecutionContext ec) {
        CallbackEvent event = new CallbackEvent(wed, ec.getParameters(), wed.getApplication());

        event.setConversationId(conversationContext.getConversationId());

        return sendAndReceive(event);
    }
}
