
package org.cucina.engine.config;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.engine.checks.DomainExpressionCheck;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DomainExpressionCheckDefinitionParser
    extends AbstractDefaultIdBeanDefinitionParser {
    public static final String DEFAULT_ID = "domainExpressionCheck";

	/**
     * JAVADOC Method Level Comments
     *
     * @param element JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return DomainExpressionCheck.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected String getDefaultId() {
        return DEFAULT_ID;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param element JAVADOC.
     * @param builder JAVADOC.
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);
    }
}
