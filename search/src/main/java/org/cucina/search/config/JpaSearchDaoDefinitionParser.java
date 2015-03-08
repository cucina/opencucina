
package org.cucina.search.config;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.config.AbstractDefaultIdBeanDefinitionParser;
import org.cucina.search.jpa.JpaSearchDao;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JpaSearchDaoDefinitionParser
    extends AbstractDefaultIdBeanDefinitionParser {
    private static final String USE_CACHE = "useCache";

    /** This is a field JAVADOC */
    public static final String DEFAULT_ID = "searchDao";

    /**
    * JAVADOC Method Level Comments
    *
    * @param element JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return JpaSearchDao.class;
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
        String uc = element.getAttribute(USE_CACHE);

        if (StringUtils.isNotEmpty(uc)) {
            builder.addPropertyValue(USE_CACHE, uc);
        }
    }
}
