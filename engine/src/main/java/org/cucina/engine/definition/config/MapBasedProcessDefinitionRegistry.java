
package org.cucina.engine.definition.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cucina.engine.definition.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;


/**
 * Simple in-memory implementation of the registry
 *
 * @author Rob Harrop
 * @author vlevine
 * @author thornton
 */
public class MapBasedProcessDefinitionRegistry
    implements ProcessDefinitionRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(MapBasedProcessDefinitionRegistry.class);
    private Map<String, ProcessDefinition> definitionStore;
    private ProcessDefinitionParser workflowDefinitionParser;

    /**
     * Creates a new MapBasedWorkflowDefinitionRegistry object.
     *
     * @param definitionResources
     *            JAVADOC.
     * @param workflowDefinitionParser
     *            JAVADOC.
     */
    public MapBasedProcessDefinitionRegistry(ProcessDefinitionParser workflowDefinitionParser) {
        Assert.notNull(workflowDefinitionParser, "workflowDefinitionParser is null");
        this.workflowDefinitionParser = workflowDefinitionParser;
        this.definitionStore = new HashMap<String, ProcessDefinition>();
    }

    /**
     * JAVADOC
     *
     * @param definitionName
     *            JAVADOC
     *
     * @return JAVADOC
     */
    @Override
    public ProcessDefinition findWorkflowDefinition(String definitionName) {
        return definitionStore.get(definitionName);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String findWorkflowSource(String definitionId) {
        // TODO workflow XML creation from workflowDefinition
        throw new RuntimeException("Method findWorkflowSource is not implemented");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<String> listWorkflowDefinitionIds() {
        return definitionStore.keySet();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionResources
     *            JAVADOC.
     */
    public void readWorkflowDefinitions(Collection<Resource> definitionResources) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Preparing to parse " + definitionResources.size() + " resources");
        }

        for (Resource definitionResource : definitionResources) {
            ProcessDefinition workflowDefinition = parseWorkflowDefinition(definitionResource);

            if (workflowDefinition == null) {
                LOG.debug("Failed to parse the resource");

                continue;
            }

            registerWorkflowDefinition(workflowDefinition);
        }
    }

    /**
     * JAVADOC
     *
     * @param workflowDefinition
     *            JAVADOC
     */
    public void registerWorkflowDefinition(ProcessDefinition workflowDefinition) {
        Assert.notNull(workflowDefinition);
        Assert.notNull(workflowDefinition.getId(), "definition's id cannot be null");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding workflow with id:" + workflowDefinition.getId());
        }

        definitionStore.put(workflowDefinition.getId(), workflowDefinition);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionResource
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    private ProcessDefinition parseWorkflowDefinition(Resource definitionResource) {
        Assert.notNull(definitionResource, "definitionResource is null");

        if (LOG.isDebugEnabled()) {
            try {
                String filename = definitionResource.getFilename();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Adding workflow definition from resource " + filename);
                }
            } catch (RuntimeException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Adding workflow definition from resource " +
                        definitionResource.getDescription());
                }
            }
        }

        return workflowDefinitionParser.parse(definitionResource);
    }
}
