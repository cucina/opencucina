package org.cucina.security.config;

import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.security.repository.jpa.PreferenceRepositoryImpl;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PreferenceRepositoryDefinitionParser
    extends AbstractDefaultIdBeanDefinitionParser {
    /** This is a field JAVADOC */
    public static final String DEFAULT_ID = "preferenceRepository";

    /**
    * JAVADOC Method Level Comments
    *
    * @param element JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return PreferenceRepositoryImpl.class;
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
}
