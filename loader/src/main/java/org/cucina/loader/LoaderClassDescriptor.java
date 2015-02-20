package org.cucina.loader;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LoaderClassDescriptor {
    /**
     *
     * @param m
     * @return
     */
    public static LoaderColumn getLoaderColumn(Method m) {
        if (m == null) {
            return null;
        }

        return m.getAnnotation(LoaderColumn.class);
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static Map<String, String> getLoaderColumnLookup(Class<?> clazz) {
        Map<String, String> ret = new HashMap<String, String>();

        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            LoaderColumnLookup lookup = f.getAnnotation(LoaderColumnLookup.class);

            if (lookup != null) {
                String[] prettyName = lookup.propertyAlias();

                if (prettyName != null) {
                    for (int j = 0; j < prettyName.length; j++) {
                        ret.put(prettyName[j], f.getName());
                    }
                }
            }
        }

        return ret;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static List<String> getLoaderColumns(Class<?> clazz) {
        if (!LoaderClassDescriptor.isLoaderColumns(clazz)) {
            return null;
        }

        // field by field
        List<String> loaderColumns = new ArrayList<String>();
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(clazz);

        for (int i = 0; i < pds.length; i++) {
            Method m = pds[i].getWriteMethod();
            LoaderColumn loaderColumn = getLoaderColumn(m);

            if (loaderColumn != null) {
                loaderColumns.add(pds[i].getName());
            }
        }

        return loaderColumns;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static boolean isLoaderColumns(Class<?> clazz) {
        LoaderColumns ann = clazz.getAnnotation(LoaderColumns.class);

        return ann != null;
    }
}
