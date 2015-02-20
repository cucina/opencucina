
package org.cucina.core.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BuilderHelper {
    /**
     * JAVADOC Method Level Comments
     *
     * @param builder
     *            JAVADOC.
     * @param value
     *            JAVADOC.
     */
    public static void addConstructorArg(BeanDefinitionBuilder builder, Element element,
        String attributeName, String propertyName) {
        builder.getRawBeanDefinition().getConstructorArgumentValues()
               .addGenericArgumentValue(buildValue(element, attributeName, propertyName, null));
    }

    /**
     * Builds a bean reference from the attribute providing a default value if
     * attribute value is empty.
     *
     * @param element
     *            JAVADOC.
     * @param attributeName
     *            JAVADOC.
     * @param propertyName
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static ValueHolder buildValue(Element element, String attributeName,
        String propertyName, Object defaultValue) {
        String uc = element.getAttribute(attributeName);
        ValueHolder vh;

        if (StringUtils.isNotEmpty(uc)) {
            vh = new ValueHolder(new RuntimeBeanReference(uc));
        } else {
            vh = new ValueHolder(defaultValue);
        }

        vh.setName(propertyName);

        return vh;
    }
}
