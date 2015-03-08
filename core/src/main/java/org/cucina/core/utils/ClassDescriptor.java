package org.cucina.core.utils;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import org.cucina.core.model.projection.DefaultGroup;
import org.cucina.core.model.projection.ExternalProjectionColumns;
import org.cucina.core.model.projection.PostProcessProjections;
import org.cucina.core.model.projection.TranslatedColumns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClassDescriptor {
    private static final Logger LOG = LoggerFactory.getLogger(ClassDescriptor.class);
    private static final Class<?>[] DEFAULT_GROUP_ARRAY = new Class<?>[] { DefaultGroup.class };

    /**
     * JAVADOC Method Level Comments
     *
     * @param clazz
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static Map<String, String> getAdditionalAliases(Class<?> clazz,
        Class<?>... requiredGroups) {
        Map<String, String> aliasByType = new HashMap<String, String>();

        // If we required multiple this would obv need to be extended....
        ExternalProjectionColumns ann = clazz.getAnnotation(ExternalProjectionColumns.class);

        if ((ann != null) && isInRequiredGroup(requiredGroups, ann.groups())) {
            aliasByType.put(ann.clazz().getSimpleName(), ann.fieldName());
        }

        return aliasByType;
    }

    /**
     *
     * @param clazz
     * @param property
     * @return
     */
    public static boolean isCollectionProperty(Class<?> clazz, String property) {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, property);

        if (pd == null) {
            return false;
        }

        Class<?> pclass = pd.getPropertyType();

        return pclass.equals(Collection.class);
    }

    /**
     *
     * @param requiredGroup
     * @param currentGroup
     * @return
     */
    public static boolean isInRequiredGroup(Class<?>[] requiredGroup, Class<?>[] currentGroup) {
        // defaults
        if ((requiredGroup != null) && (currentGroup != null) && (requiredGroup.length == 0) &&
                (currentGroup.length == 0)) {
            return true;
        }

        if ((requiredGroup != null) && (requiredGroup.length == 1) &&
                (requiredGroup[0] == Default.class) && (currentGroup.length == 0)) {
            return true;
        }

        Class<?>[] requiredGroupWithDefault = requiredGroup;

        // if no groups is specified use the default
        if ((requiredGroupWithDefault == null) || (requiredGroupWithDefault.length == 0)) {
            requiredGroupWithDefault = DEFAULT_GROUP_ARRAY;
        }

        // need to walk the interface hierarchy too
        List<Class<?>> requiredClazzs = new ArrayList<Class<?>>();

        for (Class<?> clazz : requiredGroupWithDefault) {
            if (!clazz.isInterface()) {
                // class must be an interface
                // TODO:barf
            }

            requiredClazzs.add(clazz);
            findInheritedGroups(clazz, requiredClazzs);
        }

        Class<?>[] currentGroupWithDefault = currentGroup;

        // if no groups is specified use the default
        if ((currentGroupWithDefault == null) || (currentGroupWithDefault.length == 0)) {
            currentGroupWithDefault = DEFAULT_GROUP_ARRAY;
        }

        // we don't walk the hierarchy for current group
        List<Class<?>> currentClazzs = Arrays.asList(currentGroupWithDefault);

        return CollectionUtils.containsAny(requiredClazzs, currentClazzs);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param clazz
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static boolean isPostProcessProjections(Class<?> clazz) {
        PostProcessProjections ann = clazz.getAnnotation(PostProcessProjections.class);

        return ann != null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param clazz
     *            JAVADOC.
     * @param property
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static String getPropertyType(Class<?> clazz, String property) {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, property);

        if (pd == null) {
            return null;
        }

        Class<?> pclass = pd.getPropertyType();

        if (pclass.equals(Collection.class)) {
            try {
                Field field = clazz.getDeclaredField(property);
                Type type = field.getGenericType();

                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    Type[] typeArgs = pType.getActualTypeArguments();

                    pclass = (Class<?>) typeArgs[0];
                }
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Oops", e);
                }
            }
        }

        return ClassUtils.getShortName(pclass);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param clazz
     *            JAVADOC.
     * @param property
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static boolean isTranslatedProperty(Class<?> clazz, String property) {
        if (StringUtils.isEmpty(property)) {
            return false;
        }

        TranslatedColumns tcs = clazz.getAnnotation(TranslatedColumns.class);

        if (tcs != null) {
            String[] names = tcs.value();

            if (names != null) {
                for (int i = 0; i < names.length; i++) {
                    if (property.equals(names[i])) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static void findInheritedGroups(Class<?> clazz, List<Class<?>> groups) {
        for (Class<?> inheritedGroup : clazz.getInterfaces()) {
            groups.add(inheritedGroup);
            findInheritedGroups(inheritedGroup, groups);
        }
    }
}
