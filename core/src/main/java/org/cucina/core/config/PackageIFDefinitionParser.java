
package org.cucina.core.config;

import org.cucina.core.PackageBasedInstanceFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PackageIFDefinitionParser
    extends AbstractSingleBeanDefinitionParser {
    /**
     * JAVADOC Method Level Comments
     *
     * @param element JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return PackageBasedInstanceFactory.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param element JAVADOC.
     * @param builder JAVADOC.
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        builder.addConstructorArgValue(element.getAttribute("package"));
    }
}
