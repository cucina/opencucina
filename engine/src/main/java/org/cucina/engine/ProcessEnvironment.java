package org.cucina.engine;

import java.util.Collection;

import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.BeanResolver;

import org.cucina.engine.definition.ProcessDefinitionHelper;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.service.ProcessService;


/**
 * Main object to interact to the workflow engine.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ProcessEnvironment
    extends ApplicationContextAware {
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    BeanResolver getBeanResolver();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    ProcessDefinitionParser getDefinitionParser();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    ProcessDefinitionRegistry getDefinitionRegistry();

    /**
     * Get ProcessDefinitionHelper
     *
     * @return ProcessDefinitionHelper
     */
    ProcessDefinitionHelper getProcessDefinitionHelper();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    ProcessDriverFactory getProcessDriverFactory();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    ProcessService getService();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    TokenFactory getTokenFactory();

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    String getWorkflowXml(String id);

    /**
     *
     *
     * @return .
     */
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    Collection<String> listWorkflows();
}
