
package org.cucina.core.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class AbstractDefaultIdBeanDefinitionParser
    extends AbstractSingleBeanDefinitionParser {
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    protected abstract String getDefaultId();

    /**
     * Attempts to obtain id from the value of <code>id</code> attribute,
     * failing which will default to the one provided by @see getDefaultId().
     *
     * @param element
     *            JAVADOC.
     * @param definition
     *            JAVADOC.
     * @param parserContext
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws BeanDefinitionStoreException
     *             JAVADOC.
     */
    @Override
    public final String resolveId(Element element, AbstractBeanDefinition definition,
        ParserContext parserContext)
        throws BeanDefinitionStoreException {
        if (!parserContext.isNested()) {
            return ((element != null) && StringUtils.isNotEmpty(element.getAttribute("id")))
            ? element.getAttribute("id") : getDefaultId();
        }

        return null;
    }
}
