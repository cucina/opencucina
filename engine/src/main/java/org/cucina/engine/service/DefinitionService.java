package org.cucina.engine.service;

import java.util.Collection;

import org.springframework.validation.BindException;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.model.Workflow;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface DefinitionService {
    /**
    * Creates a new workflow
    *
    * @param fileName JAVADOC.
    * @param contentType JAVADOC.
    * @param content JAVADOC.
    *
    * @return JAVADOC.
    * @throws BindException
    */
    Workflow create(String fileName, String contentType, byte[] content)
        throws BindException;

    /**
     * Delete workflow definition
     *
     * @param id JAVADOC.
     *
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
     *
     * @return JAVADOC.
     */
    ProcessDefinition loadDefinition(String id);

    /**
     * Updates existing workflow
     *
     * @param fileName JAVADOC.
     * @param contentType JAVADOC.
     * @param content JAVADOC.
     *
     * @return JAVADOC.
     * @throws BindException
     */
    Workflow update(String fileName, String contentType, byte[] content)
        throws BindException;
}
