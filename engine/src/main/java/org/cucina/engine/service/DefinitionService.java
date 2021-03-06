package org.cucina.engine.service;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;
import org.springframework.validation.BindException;

import java.util.Collection;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface DefinitionService {
	/**
	 * Saves the new process definition
	 *
	 * @param fileName    JAVADOC.
	 * @param contentType JAVADOC.
	 * @param content     JAVADOC.
	 * @return JAVADOC.
	 * @throws BindException
	 */
	Workflow save(String fileName, String contentType, byte[] content)
			throws BindException;

	/**
	 * Delete workflow definition
	 *
	 * @param id JAVADOC.
	 * @throws BindException JAVADOC.
	 */
	void delete(String id)
			throws BindException;

	/**
	 * List all definitions
	 *
	 * @return .
	 */
	Collection<String> list();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id JAVADOC.
	 * @return JAVADOC.
	 */
	ProcessDefinition loadDefinition(String id);
}
