package org.cucina.engine.definition.config;

import java.util.Collection;

import org.cucina.core.model.Attachment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;


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
     *
     *
     * @param workflowId .
     */
    void delete(String workflowId) throws BindException;

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
     * @return Collection of all registered workflow definitions.
     */
    Collection<String> listWorkflowDefinitionIds();

    /**
     * Reads workflow definitions from the provided list of resources.
     *
     * @param resources
     *            JAVADOC.
     */
    void readWorkflowDefinitions(Collection<Resource> resources);


	Workflow createProcess(Attachment attachment) throws BindException;

    /**
     * Updates an existing process.
     *
     * @param history .
     */
	Workflow updateProcess(Attachment attachment) throws BindException;
}
