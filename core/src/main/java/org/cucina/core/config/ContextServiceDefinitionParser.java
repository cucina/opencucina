
package org.cucina.core.config;

import org.cucina.core.service.ThreadLocalContextService;
import org.cucina.core.spring.SingletonBeanFactory;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ContextServiceDefinitionParser
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
        return ThreadLocalContextService.class;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    protected String getDefaultId() {
        return SingletonBeanFactory.CONTEXT_SERVICE_ID;
    }
}
