
package org.cucina.engine.config;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.core.config.BuilderHelper;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.engine.TokenFactoryImpl;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class TokenFactoryDefinitionParser
    extends AbstractDefaultIdBeanDefinitionParser {
    /**
     * JAVADOC Method Level Comments
     *
     * @param element JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return TokenFactoryImpl.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected String getDefaultId() {
        return "tokenFactory";
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param element JAVADOC.
     * @param builder JAVADOC.
     */
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        BuilderHelper.addConstructorArg(builder, element, SingletonBeanFactory.INSTANCE_FACTORY_ID,
            SingletonBeanFactory.INSTANCE_FACTORY_ID);
        BuilderHelper.addConstructorArg(builder, element, "tokenRepository",
        		"tokenRepository");
        builder.addPropertyValue("tokenClass", element.getAttribute("tokenClass"));
    }
}
