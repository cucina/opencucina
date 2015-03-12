
package org.cucina.engine.definition.config;

import java.util.Collection;

import org.cucina.engine.definition.ProcessDefinition;
import org.springframework.core.io.Resource;


/**
 * Registry for storing {@link ProcessDefinition workflow definitions}. Using a
 * <code>WorkflowDefinitionRegistry</code> {@link ProcessDefinition workflow
 * definitions} can be looked up using the definition ID.
 *
 * @author Rob Harrop
 * @author vlevine
 * @see ProcessDefinition
 */
public interface ProcessDefinitionRegistry {
    /**
     * Finds a {@link ProcessDefinition} using the specified definition ID.
     */
    ProcessDefinition findWorkflowDefinition(String definitionId);

    /**
     * Loads xml source for the workflow
     * @param definitionId
     * @return
     */
    String findWorkflowSource(String definitionId);

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    /**
     * @return Collection of all registered workflow definitions
     */
    Collection<String> listWorkflowDefinitionIds();

    /**
     * Reads workflow definitions from the provided list of resources.
     *
     * @param resources
     *            JAVADOC.
     */
    void readWorkflowDefinitions(Collection<Resource> resources);

    /**
     * Sets the WorkflowDefinition in the registry or updates if exists already.
     *
     * @param definition
     *            WorkflowDefinition.
     */
    void registerWorkflowDefinition(ProcessDefinition definition);
}
