package org.cucina.engine.config;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.engine.repository.jpa.WorkflowRepositoryImpl;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class WorkflowRepositoryDefinitionParser
    extends AbstractDefaultIdBeanDefinitionParser {
    private static final String WORKFLOW_REPO = "workflowRepository";

    /**
    * JAVADOC Method Level Comments
    *
    * @param element
    *            JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return WorkflowRepositoryImpl.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected String getDefaultId() {
        return WORKFLOW_REPO;
    }
}
