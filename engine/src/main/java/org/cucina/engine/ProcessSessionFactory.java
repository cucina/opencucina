package org.cucina.engine;

import org.cucina.engine.definition.ProcessDefinition;


/**
 * Thread-safe, expensive to create object that provides access to
 * {@link org.cucina.engine.ProcessSession workflow sessions}.
 *
 * @author Rob Harrop
 */
public interface ProcessSessionFactory {
	/**
	 * Opens a new {@link ProcessSession} for interacting with the workflow engine. All interactions
	 * are executed against the supplied {@link ProcessDefinition}.
	 */
	ProcessSession openSession(ProcessDefinition workflowDefinition);

	/**
	 * Open a new {@link ProcessSession} for interacting with the workflow engine. All
	 * interactions are executed against the {@link ProcessDefinition} identified by the
	 * supplied workflow definition ID.
	 */
	ProcessSession openSession(String workflowDefinitionId);
}
