
package org.cucina.engine;

import java.util.Collection;

import org.cucina.engine.definition.ProcessDefinitionHelper;
import org.cucina.engine.definition.config.ProcessDefinitionParser;
import org.cucina.engine.definition.config.ProcessDefinitionRegistry;
import org.cucina.engine.service.ProcessService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.expression.BeanResolver;


/**
 * Main object to interact to the workflow engine.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ProcessEnvironment
    extends ApplicationContextAware {
    /**
     * Should return the ApplicationContext which is set by ApplicationContextAware.setApplicationContext
     *
     * @return JAVADOC.
     */
    ApplicationContext getApplicationContext();

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
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    ProcessDriverFactory getExecutorFactory();

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
     * Get ProcessDefinitionHelper
     *
     * @return ProcessDefinitionHelper
     */
    ProcessDefinitionHelper getProcessDefinitionHelper();

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    String getWorkflowXml(String id);

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
