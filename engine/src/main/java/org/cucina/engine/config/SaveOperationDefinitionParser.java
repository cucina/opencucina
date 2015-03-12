package org.cucina.engine.config;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;

import org.cucina.engine.operations.DeleteOperation;
import org.cucina.engine.operations.SaveOperation;

import org.w3c.dom.Element;


/**
 * Parser for {@link DeleteOperation}.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SaveOperationDefinitionParser
    extends AbstractDefaultIdBeanDefinitionParser {
    /** This is a field JAVADOC */
    public static final String DEFAULT_ID = "saveOperation";

    /**
     * Get bean class for which this parser parses.
     *
     * @param element Element.
     *
     * @return DeleteAction.class.
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return SaveOperation.class;
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
     * Parses and gets element for SaveAction.
     *
     * @param element JAVADOC.
     * @param builder JAVADOC.
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        builder.setScope(BeanDefinition.SCOPE_PROTOTYPE);

        String ee = element.getAttribute("domainRepository");

        if (StringUtils.isNotEmpty(ee)) {
            builder.addConstructorArgReference(ee);
        }
    }
}
