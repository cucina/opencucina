package org.cucina.core.config;

import org.cucina.core.repository.jpa.ListNodeRepositoryImpl;
import org.cucina.core.spring.SingletonBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ListNodeRepositoryDefinitionParser
    extends AbstractDefaultIdBeanDefinitionParser {
    /** This is a field JAVADOC */
    public static final String DEFAULT_ID = "listNodeRepository";

    /**
    * JAVADOC Method Level Comments
    *
    * @param element JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return ListNodeRepositoryImpl.class;
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
        BuilderHelper.addConstructorArg(builder, element, SingletonBeanFactory.I18N_SERVICE_ID,
            SingletonBeanFactory.I18N_SERVICE_ID);
    }
}
