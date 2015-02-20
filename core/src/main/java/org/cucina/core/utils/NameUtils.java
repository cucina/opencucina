
package org.cucina.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public final class NameUtils {
    /** Separator for bean property names (period). */
    public static final String DEFAULT_SEPARATOR = ".";

    /**
     * Extract the property name separated from the type of the most significant property.
     * Thus if we have a property name <code>parent.owner.name</code> return <code>owner</code>.
     *
     * @param property the name of the property to parse (should not be null).
     *
     * @return The most second last bean property name, or property if input contains no periods.
     * @deprecated until found a use
     */
    public static String getAlias(String property) {
        Assert.notNull(property);

        String[] tokens = StringUtils.split(property, DEFAULT_SEPARATOR);

        if (tokens.length < 2) {
            return property;
        }

        return tokens[tokens.length - 2];
    }

    /**
     * Extract the property of the property String. Thus if we have a property name
     * <code>RISK.owner.name</code> return <code>name</code>.
     *
     * @param property the name of the property to parse (should not be null).
     *
     * @return The final bean property name, or property if input contains no periods.
     * @deprecated until found a use
     */
    public static String getName(String property) {
        Assert.notNull(property);

        String[] tokens = StringUtils.split(property, DEFAULT_SEPARATOR);

        if (tokens.length < 2) {
            return property;
        }

        return tokens[tokens.length - 1];
    }

    /**
     * Extract the type, which should be the first property. Thus if we have a property
     * name <code>TYPE.owner.name</code> return <code>TYPE</code>.
     *
     * @param property the name of the property to parse (should not be null).
     *
     * @return The first property in sequence, separated by '.'
     * @deprecated until found a use
     */
    public static String getType(String property) {
        Assert.notNull(property);

        String[] tokens = StringUtils.split(property, DEFAULT_SEPARATOR);

        if (tokens.length < 2) {
            return null;
        }

        return tokens[0];
    }

    /**
     *
     * @return The base and name values concatenated with a period. If base ends with a period a
     *         new one is not added.
     */
    public static String concat(String base, String name) {
        if (StringUtils.isEmpty(base)) {
            return name;
        }

        return base + (base.endsWith(DEFAULT_SEPARATOR) ? "" : DEFAULT_SEPARATOR) + name;
    }
}
