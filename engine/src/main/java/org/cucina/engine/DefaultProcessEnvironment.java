
package org.cucina.engine;

import java.util.Collection;
import java.util.List;

import org.cucina.core.spring.ELExpressionExecutor;
import org.cucina.core.spring.ExpressionExecutor;
import org.cucina.core.spring.ProtocolResolver;
import org.cucina.engine.definition.WorkflowDefinitionHelper;
import org.cucina.engine.definition.WorkflowDefinitionHelperImpl;
import org.cucina.engine.definition.config.MapBasedProcessDefinitionRegistry;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.definition.config.xml.DigesterModuleProcessDefinitionParser;
import org.cucina.engine.service.DefaultWorkflowService;
import org.cucina.engine.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.io.Resource;
import org.springframework.expression.BeanResolver;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;


/**
 * This is default implementation of environment. Most of its functionality
 * relies upon pluggable components. Unless these components defined in the
 * Spring context as beans with following 'magic' names, this environment will
 * construct workable default implementations. So the environment can be used
 * out-of-the-box.
 *
 * <code>workflowDefinitionRegistry</code>
 *
 * <code>workflowDefinitionReader</code>
 *
 * <code>workflowDefinitionParser</code>
 *
 * <code>workflowSessionFactory</code>
 *
 * <code>workflowService</code>
 *
 * <code>workflowDefinitionHelper</code>
 *
 * <code>expressionExecutor</code>
 *
 * The only mandatory property id <code>tokenFactory</code>, implementation of
 * which must be provided by the user.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@ManagedResource
public class DefaultProcessEnvironment
    implements ProcessEnvironment, SmartLifecycle {
    private static final String EXPRESSION_EXECUTOR = "expressionExecutor";
    private static final String WORKFLOW_EXECUTOR = "workflowExecutor";
    private static final String EXECUTOR_FACTORY = "executorFactory";
    private static final String RULES_DEFINITION_URL = "classpath:org/cucina/engine/definition/config/xml/workflow-rules-definitions.xml";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultProcessEnvironment.class);
    private static final String DEFINITION_REGISTRY = "workflowDefinitionRegistry";
    private static final String DEFINITION_PARSER = "workflowDefinitionParser";
    private static final String DEFINITION_HELPER = "workflowDefinitionHelper";
    private static final String SESSION_FACTORY = "workflowSessionFactory";
    private static final String SERVICE = "workflowService";
    private static final String RESOLVER = "beanResolver";
    private static ProcessEnvironment instance;
    private static final int DEFAULT_LIFECYCLE_PHASE = 100;
    private ApplicationContext applicationContext;
    private BeanResolver beanResolver;
    private Collection<Resource> definitionResources;
    private ProcessDriverFactory executorFactory;
    private List<WorkflowListener> workflowListeners;
    private TokenFactory tokenFactory;
    private WorkflowDefinitionHelper workflowDefinitionHelper;
    private ProcessDefinitionParser definitionParser;
    private ProcessDefinitionRegistry definitionRegistry;
    private WorkflowService workflowService;
    private ProcessSessionFactory workflowSessionFactory;
    private boolean running;

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     *
     * @throws BeansException
     *             JAVADOC.
     */
    @Override
    public void setApplicationContext(ApplicationContext arg0)
        throws BeansException {
        this.applicationContext = arg0;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isAutoStartup() {
        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public BeanResolver getBeanResolver() {
        return beanResolver;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public ProcessDefinitionParser getDefinitionParser() {
        return definitionParser;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public ProcessDefinitionRegistry getDefinitionRegistry() {
        return definitionRegistry;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionResources
     *            JAVADOC.
     */
    @Required
    public void setDefinitionResources(Collection<Resource> definitionResources) {
        this.definitionResources = definitionResources;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param executorFactory JAVADOC.
     */
    public void setExecutorFactory(ProcessDriverFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public ProcessDriverFactory getExecutorFactory() {
        return executorFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public int getPhase() {
        return DEFAULT_LIFECYCLE_PHASE;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public WorkflowService getService() {
        return workflowService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param tokenFactory
     *            JAVADOC.
     */
    @Required
    public void setTokenFactory(TokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public TokenFactory getTokenFactory() {
        return tokenFactory;
    }

    /**
     * Get workflowDefinitionHelper
     *
     * @return workflowDefinitionHelper
     */
    @Override
    public WorkflowDefinitionHelper getWorkflowDefinitionHelper() {
        return workflowDefinitionHelper;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflowListeners
     *            JAVADOC.
     */
    public void setWorkflowListeners(List<WorkflowListener> workflowListeners) {
        this.workflowListeners = workflowListeners;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @ManagedOperation
    @Override
    public String getWorkflowXml(String id) {
        try {
            return getDefinitionRegistry().findWorkflowSource(id);
        } catch (RuntimeException e) {
            LOG.error("Oops", e);
            throw e;
        }
    }

    /**
     * Singleton style access for objects created on a stack.
     *
     * @return An instance of this.
     * @throws IllegalArgumentException
     *             if the instance has not been initialised yet.
     */
    public static ProcessEnvironment instance() {
        if (instance == null) {
            throw new IllegalArgumentException("The workflow environment is not initialized yet");
        }

        return instance;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ManagedOperation
    @Override
    public Collection<String> listWorkflows() {
        return getDefinitionRegistry().listWorkflowDefinitionIds();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Override
    public void start() {
        Assert.notNull(applicationContext, "applicationContext is null");
        instance = this;
        initResolverObjectFactory();
        initDefinitionParser();
        initDefinitionRegistry();
        initSessionFactory();
        initService();
        initDefinitionHelper();
        running = true;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Started");
        }
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Override
    public void stop() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Stop called from:", new Exception());
        }

        running = false;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param runnable JAVADOC.
     */
    @Override
    public void stop(Runnable runnable) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Stop called for " + runnable + " from:", new Exception());
        }

        runnable.run();
        running = false;
    }

    @SuppressWarnings("unchecked")
    private <T> T findInjectedBean(String name, Class<T> clazz) {
        if (applicationContext.containsBean(name)) {
            LOG.debug("Evaluating plugged " + name);

            Object obj = applicationContext.getBean(name);

            if ((obj != null) && clazz.isAssignableFrom(obj.getClass())) {
                return (T) obj;
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Did not find bean '" + name + "' of class " + clazz);
        }

        return null;
    }

    private void initDefinitionHelper() {
        WorkflowDefinitionHelper proto = findInjectedBean(DEFINITION_HELPER,
                WorkflowDefinitionHelper.class);

        if (proto != null) {
            LOG.debug("Using plugged " + DEFINITION_HELPER);

            workflowDefinitionHelper = proto;

            return;
        }

        workflowDefinitionHelper = new WorkflowDefinitionHelperImpl(definitionRegistry);
    }

    private void initDefinitionParser() {
        ProcessDefinitionParser proto = findInjectedBean(DEFINITION_PARSER,
                ProcessDefinitionParser.class);

        if (proto != null) {
            LOG.debug("Using plugged " + DEFINITION_PARSER);

            definitionParser = proto;

            return;
        }

        definitionParser = new DigesterModuleProcessDefinitionParser(applicationContext.getResource(
                    RULES_DEFINITION_URL));
    }

    private void initDefinitionRegistry() {
        ProcessDefinitionRegistry proto = findInjectedBean(DEFINITION_REGISTRY,
                ProcessDefinitionRegistry.class);

        if (proto != null) {
            LOG.debug("Using plugged " + DEFINITION_REGISTRY);

            definitionRegistry = proto;
        } else {
            definitionRegistry = new MapBasedProcessDefinitionRegistry(definitionParser);
        }

        definitionRegistry.readWorkflowDefinitions(definitionResources);
    }

    private void initResolverObjectFactory() {
        BeanResolver proto = findInjectedBean(RESOLVER, BeanResolver.class);

        if (proto != null) {
            LOG.debug("Using plugged " + RESOLVER);

            beanResolver = proto;

            return;
        }

        beanResolver = new ProtocolResolver();

        try {
            ((ProtocolResolver) beanResolver).setApplicationContext(applicationContext);
            ((ProtocolResolver) beanResolver).afterPropertiesSet();
        } catch (Exception e) {
            LOG.error("Oops", e);
            throw new IllegalArgumentException("Oops", e);
        }
    }

    private void initService() {
        WorkflowService proto = findInjectedBean(SERVICE, WorkflowService.class);

        if (proto != null) {
            LOG.debug("Using plugged " + SERVICE);

            workflowService = proto;

            return;
        }

        workflowService = new DefaultWorkflowService(workflowSessionFactory);
    }

    private void initSessionFactory() {
        ProcessSessionFactory proto = findInjectedBean(SESSION_FACTORY,
                ProcessSessionFactory.class);

        if (proto != null) {
            LOG.debug("Using plugged " + SESSION_FACTORY);

            workflowSessionFactory = proto;

            return;
        }

        if (executorFactory == null) {
            ProcessDriverFactory eproto = findInjectedBean(EXECUTOR_FACTORY, ProcessDriverFactory.class);

            if (eproto == null) {
                ProcessDriver executor = findInjectedBean(WORKFLOW_EXECUTOR,
                        ProcessDriver.class);

                ExpressionExecutor eex = findInjectedBean(EXPRESSION_EXECUTOR,
                        ExpressionExecutor.class);

                if (eex == null) {
                    eex = new ELExpressionExecutor();
                    ((ELExpressionExecutor) eex).setBeanFactory(applicationContext);
                }

                eproto = new ProcessDriverFactoryImpl(tokenFactory, eex,
                        (executor == null) ? new LocalProcessDriver() : executor);
            }

            executorFactory = eproto;
        }

        this.workflowSessionFactory = new DefaultProcessSessionFactory(definitionRegistry,
                workflowListeners, executorFactory);
    }
}
