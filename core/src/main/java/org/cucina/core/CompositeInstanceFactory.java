package org.cucina.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cucina.core.utils.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CompositeInstanceFactory
    extends AbstractInstanceFactory {
    private static final String I18N_KEY = "i18n:";
    private static final Logger LOG = LoggerFactory.getLogger(CompositeInstanceFactory.class);
    private List<InstanceFactory> instanceFactories = new ArrayList<InstanceFactory>();
    private Map<String, Boolean> attrCache = new HashMap<String, Boolean>();
    private Map<String, String> ptCache = new HashMap<String, String>();

    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public <T> T getBean(String type) {
        for (InstanceFactory instfac : instanceFactories) {
            @SuppressWarnings("unchecked")
            T t = (T) instfac.getBean(type);

            if (t != null) {
                return t;
            }
        }

        LOG.info("Failed to find bean with name '" + type + "'");

        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param property JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isI18nProperty(String className, String property) {
        String cacheKey = I18N_KEY + NameUtils.concat(className, property);
        Boolean isI18n = attrCache.get(cacheKey);

        if (isI18n == null) {
            isI18n = false;

            for (InstanceFactory instfac : instanceFactories) {
                isI18n = instfac.isI18nProperty(className, property);

                if (isI18n) {
                    break;
                }
            }

            attrCache.put(cacheKey, isI18n);
        }

        return isI18n;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param instanceFactories JAVADOC.
     */
    public void setInstanceFactories(List<InstanceFactory> instanceFactories) {
        this.instanceFactories = instanceFactories;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param property JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String getPropertyType(String className, String property) {
        String cacheKey = NameUtils.concat(className, property);
        String type = ptCache.get(cacheKey);

        if (type == null) {
            // If nested go down the hierarchy
            if (property.indexOf('.') > 0) {
                LOG.debug("Nester property name '" + property + "'");

                String latestPropertyName = property.substring(0, property.indexOf('.'));
                String latestType = getPropertyType(className, latestPropertyName);

                if (latestType == null) {
                    LOG.debug("Could not workout nested Class type for " + property);

                    return null;
                }

                String nextPropertyName = property.substring(property.indexOf('.') + 1);

                type = getPropertyType(latestType, nextPropertyName);
            } else {
                for (InstanceFactory instfac : instanceFactories) {
                    type = instfac.getPropertyType(className, property);

                    if (type != null) {
                        break;
                    }
                }
            }
        }

        if (type == null) {
            LOG.info("Failed to determine property type for name '" + property + "'");
        }

        ptCache.put(cacheKey, type);

        return type;
    }

    /**
     * Assumes the fully qualified name. TODO make special cases for String and Date
     *
     * @param <T> JAVADOC.
     * @param name JAVADOC.
     *
     * @return JAVADOC.
     */
    protected <T> Class<T> doGetClassType(String name) {
        for (InstanceFactory instfac : instanceFactories) {
            Class<T> clazz = instfac.getClassType(name);

            if (clazz != null) {
                return clazz;
            }
        }

        LOG.info("Failed to determine class for type name '" + name + "'");

        return null;
    }
}
