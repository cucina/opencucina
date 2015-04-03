package org.cucina.engine.server.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.ConversionService;
import org.springframework.integration.gateway.MessagingGatewaySupport;
import org.springframework.util.Assert;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.LocalProcessDriver;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.definition.Check;
import org.cucina.engine.definition.Operation;
import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.definition.CheckDescriptor;
import org.cucina.engine.server.definition.OperationDescriptor;
import org.cucina.engine.server.event.BooleanEvent;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.RollbackEvent;

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
    private ProcessDriver localDriver;

    /**
    * Creates a new MessagingWorkflowDriver object.
    *
    * @param conversationContext JAVADOC.
    * @param localDriver JAVADOC.
    * @param applicationName JAVADOC.
    */
    public MessagingProcessDriver(ConversationContext conversationContext) {
        Assert.notNull(conversationContext, "conversationContext is null");
        this.conversationContext = conversationContext;
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
                OperationDescriptor wed = (OperationDescriptor) action;
                BeanWrapper beanWrapper = new BeanWrapperImpl(executionContext.getToken()
                                                                              .getDomainObject());

                wed.setDomainType((String) beanWrapper.getPropertyValue("applicationType"));
                wed.setDomainId(executionContext.getToken().getDomainObject().getId());

                String application = wed.getApplication();

                if (StringUtils.isEmpty(application)) {
                    executeLocal(wed, executionContext);
                } else {
                    CallbackEvent event = new CallbackEvent(wed, executionContext.getParameters(),
                            application);

                    event.setConversationId(conversationContext.getConversationId());

                    Object result = sendAndReceive(event);

                    LOG.debug("Action result is :'" + result + "'");

                    if (result instanceof Map<?, ?>) {
                        executionContext.getParameters()
                                        .putAll((Map<?extends String, ?extends Object>) result);

                        continue;
                    }
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

            BeanWrapper beanWrapper = new BeanWrapperImpl(executionContext.getToken()
                                                                          .getDomainObject());

            ced.setDomainType((String) beanWrapper.getPropertyValue("applicationType"));
            ced.setDomainId(executionContext.getToken().getDomainObject().getId());

            String application = ced.getApplication();

            if (StringUtils.isEmpty(application)) {
                return testLocal(ced, executionContext);
            }

            CallbackEvent event = new CallbackEvent(ced, executionContext.getParameters(),
                    application);

            event.setConversationId(conversationContext.getConversationId());

            Object result = sendAndReceive(event);

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

    private void executeLocal(OperationDescriptor op, ExecutionContext context) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Local execution");
        }

        localDriver.execute(Collections.<Operation>singletonList(op), context);
    }

    private boolean testLocal(CheckDescriptor check, ExecutionContext context) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Local test");
        }

        return localDriver.test(check, context);
    }
}
