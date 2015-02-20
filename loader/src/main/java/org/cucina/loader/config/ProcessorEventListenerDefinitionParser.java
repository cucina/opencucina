package org.cucina.loader.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.core.config.BuilderHelper;

import org.cucina.loader.processor.ProcessorEventListener;

import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ProcessorEventListenerDefinitionParser
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
        return ProcessorEventListener.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected String getDefaultId() {
        return "processorEventListener";
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param element JAVADOC.
     * @param builder JAVADOC.
     */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        BuilderHelper.addConstructorArg(builder, element, "processor", "processor");
    }
}
