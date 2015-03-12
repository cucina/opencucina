package org.cucina.engine.config;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.engine.service.DefinitionServiceImpl;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: vlevine
 * @version $Revision: $
 */
public class DefinitionServiceDefinitionParser
    extends AbstractDefaultIdBeanDefinitionParser {
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
        return DefinitionServiceImpl.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected String getDefaultId() {
        return "definitionService";
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param element
     *            JAVADOC.
     * @param builder
     *            JAVADOC.
     */
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        // assume that spring will do the right thing here, otherwise use code
        // above (sigh)
        builder.addConstructorArgReference(element.getAttribute("workflowEnvironment"))
               .addConstructorArgReference(element.getAttribute("instanceFactory"))
               .addConstructorArgReference(element.getAttribute("workflowRepository"))
               .addPropertyReference("validator", element.getAttribute("validator"));
    }
}
