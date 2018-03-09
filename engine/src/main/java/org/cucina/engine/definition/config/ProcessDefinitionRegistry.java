package org.cucina.engine.definition.config;

import org.cucina.core.model.Attachment;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import java.util.Collection;

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
	 * Loads xml source for the workflow
	 */
	String getWorkflowSource(String definitionId);

	void delete(String workflowId) throws BindException;

	/**
	 * Finds a {@link ProcessDefinition} using the specified definition ID.
	 */
	ProcessDefinition findWorkflowDefinition(String definitionId);

	/**
	 * @return Collection of all registered workflow definitions.
	 */
	Collection<String> listWorkflowDefinitionIds();

	/**
	 * Reads workflow definitions from the provided list of resources.
	 */
	void readWorkflowDefinitions(Collection<Resource> resources);

	/**
	 * Saves the definition by either updating or creating, dependent on whether
	 * there is an existing one with the same name.
	 */
	Workflow saveProcess(Attachment attachment) throws BindException;
}
