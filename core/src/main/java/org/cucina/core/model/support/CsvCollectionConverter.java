package org.cucina.core.model.support;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * @deprecated
 */
@SuppressWarnings("rawtypes")
@Converter
public class CsvCollectionConverter
    implements AttributeConverter<Collection, String> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param val
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public String convertToDatabaseColumn(Collection val) {
        if (val == null) {
            return null;
        }

        return toString(val);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param val
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection convertToEntityAttribute(String val) {
        if (val == null) {
            return null;
        }

        return fromString(val);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param string
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static Collection<String> fromString(String string) {
        String[] vals = StringUtils.split(string, ", ");

        return Arrays.asList(vals);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param coll
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static String toString(Collection<String> coll) {
        if (CollectionUtils.isEmpty(coll)) {
            return null;
        }

        return StringUtils.join(coll, ',');
    }
}
