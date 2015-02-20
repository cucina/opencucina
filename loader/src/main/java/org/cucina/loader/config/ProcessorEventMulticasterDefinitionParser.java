package org.cucina.loader.config;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;

import org.cucina.loader.processor.ProcessorEventMulticaster;

import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ProcessorEventMulticasterDefinitionParser
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
        return ProcessorEventMulticaster.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected String getDefaultId() {
        return "processorEventMulticaster";
    }
}
