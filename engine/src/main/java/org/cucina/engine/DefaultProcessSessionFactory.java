
package org.cucina.engine;

import java.util.List;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * Default implementation of {@link ProcessSessionFactory} that simply creates
 * instances of {@link DefaultProcessSession} passing in a
 * {@link ProcessDefinition} and {@link WorkflowInstanceHandle}.
 *
 * @author Rob Harrop
 * @author vlevine
 * @see ProcessDefinition
 * @see WorkflowInstanceHandle
 */
public class DefaultProcessSessionFactory
    implements ProcessSessionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultProcessSessionFactory.class);
    private ProcessDriverFactory executorFactory;
    private List<WorkflowListener> workflowListeners;

    /**
     * The {@link ProcessDefinitionRegistry} used to lookup
     * {@link ProcessDefinition WorkflowDefinitions}.
     */
    private ProcessDefinitionRegistry definitionRegistry;

    /**
     * Creates a new DefaultWorkflowSessionFactory object.
     *
     * @param tokenFactory mandatory.
     * @param workflowDefinitionRegistry mandatory.
     */
    public DefaultProcessSessionFactory(ProcessDefinitionRegistry workflowDefinitionRegistry,
        List<WorkflowListener> workflowListeners, ProcessDriverFactory executorFactory) {
        Assert.notNull(workflowDefinitionRegistry, "workflowDefinitionRegistry is null");
        this.definitionRegistry = workflowDefinitionRegistry;
        this.workflowListeners = workflowListeners;
        Assert.notNull(executorFactory, "executorFactory is null");
        this.executorFactory = executorFactory;
    }

    /**
     * Opens a new {@link ProcessSession} for interacting with the workflow
     * engine. All interactions are executed against the supplied
     * {@link ProcessDefinition}.
     */
    public ProcessSession openSession(ProcessDefinition workflowDefinition) {
        return createWorkflowSession(workflowDefinition);
    }

    /**
     * Open a new {@link ProcessSession} for interacting with the workflow
     * engine. All interactions are executed against the
     * {@link ProcessDefinition} identified by the supplied workflow definition
     * ID.
     */
    public ProcessSession openSession(String workflowDefinitionId) {
        ProcessDefinition wd = definitionRegistry.findWorkflowDefinition(workflowDefinitionId);

        Assert.notNull(wd,
            "Could not find workflow definition with name='" + workflowDefinitionId + "'");

        if (LOG.isDebugEnabled()) {
            LOG.debug("workflowDefinition: " + wd);
        }

        return createWorkflowSession(wd);
    }

    /**
     * Creates a new instance of {@link DefaultProcessSession} for the supplied
     * {@link ProcessDefinition} and {@link WorkflowInstanceHandle}.
     */
    protected ProcessSession createWorkflowSession(ProcessDefinition workflowDefinition) {
        if (workflowListeners != null) {
            for (WorkflowListener workflowListener : workflowListeners) {
                workflowListener.startingSession(workflowDefinition);
            }
        }

        DefaultProcessSession workflowSession = new DefaultProcessSession(workflowDefinition,
                executorFactory, workflowListeners);

        return workflowSession;
    }
}
