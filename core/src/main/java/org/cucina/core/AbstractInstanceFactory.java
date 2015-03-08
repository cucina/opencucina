package org.cucina.core;

import java.util.HashMap;
import java.util.Map;

import org.cucina.core.utils.ClassDescriptor;
import org.cucina.core.utils.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class AbstractInstanceFactory
    implements InstanceFactory {
    private static final String FK_KEY = "fk:";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractInstanceFactory.class);
    private Map<String, Boolean> attrCache = new HashMap<String, Boolean>();
    @SuppressWarnings("rawtypes")
    private Map<String, Class> cache = new HashMap<String, Class>();

    /**
     * Does some caching
     *
     * @param <T> JAVADOC.
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<T> getClassType(String type) {
        Class<T> clazz = cache.get(type);

        if (clazz == null) {
            clazz = doGetClassType(type);
            cache.put(type, clazz);
        }

        return clazz;
    }

    /**
     * Simple implementation, if the propertyType returned from getPropertyType
     * is a known class then return true. (non-Javadoc)
     *
     * @see org.cucina.meringue.core.InstanceFactory#isForeignKey(java.lang.String)
     *
     * @param property
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isForeignKey(String className, String property) {
        String cacheKey = FK_KEY + NameUtils.concat(className, property);
        Boolean isFk = attrCache.get(cacheKey);

        if (isFk == null) {
            String propertyType = getPropertyType(className, property);

            isFk = !ClassDescriptor.isCollectionProperty(getClassType(className), property) &&
                (getClassType(propertyType) != null);
            attrCache.put(cacheKey, isFk);
        }

        return isFk;
    }

    /**
     * Return true if i18nProperties contains property
     *
     * @param property
     *            type.propertyName.
     *
     * @return true/false if property internationalised.
     */
    @Override
    public boolean isTranslatedProperty(String className, String property) {
        Class<?> t = getClassType(className);

        return (t == null) ? false : ClassDescriptor.isTranslatedProperty(t, property);
    }

    /**
     * First part of the property before the . should be the type name as in
     * getBean() method
     *
     * @param property
     *            type.propertyName.
     *
     * @return JAVADOC.
     */
    @Override
    public String getPropertyType(String className, String property) {
        Class<?> clazz = getClassType(className);

        if (clazz == null) {
            // not in this instance factory
            return null;
        }

        // If nested go down the hierarchy
        if (property.indexOf('.') > 0) {
            LOG.debug("Nester property name '" + property + "'");

            String latestPropertyName = property.substring(0, property.indexOf('.'));
            String latestType = ClassDescriptor.getPropertyType(clazz, latestPropertyName);

            String nextPropertyName = property.substring(property.indexOf('.') + 1);

            return getPropertyType(latestType, nextPropertyName);
        }

        return ClassDescriptor.getPropertyType(clazz, property);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    protected abstract <T> Class<T> doGetClassType(String type);
}
