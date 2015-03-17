package org.cucina.engine.server.service;

import java.util.Map;

import org.cucina.engine.definition.Token;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface StartWorkflowService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationType JAVADOC.
     * @param id JAVADOC.
     * @param applicationName JAVADOC.
     * @param parameters JAVADOC.
     *
     * @return JAVADOC.
     */
    Token startWorkflow(String applicationType, Long id, String applicationName,
        Map<String, Object> parameters);
}
