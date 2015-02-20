
package org.cucina.core.model.eclipselink;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.mappings.foundation.AbstractDirectMapping;
import org.eclipse.persistence.sessions.Session;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CsvCollectionConverter
    implements Converter {
    private static final long serialVersionUID = -4544057848189055214L;

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

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isMutable() {
        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param val JAVADOC.
     * @param session JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Object convertDataValueToObjectValue(Object val, Session session) {
        if (val == null) {
            return null;
        }

        return fromString(val.toString());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param val JAVADOC.
     * @param session JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object convertObjectValueToDataValue(Object val, Session session) {
        if (val == null) {
            return null;
        }

        if (!(val instanceof Collection<?>)) {
            return "Not a Collection";
        }

        return toString((Collection<String>) val);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param mapping JAVADOC.
     * @param session JAVADOC.
     */
    @Override
    public void initialize(DatabaseMapping mapping, Session session) {
        if (mapping.isAbstractDirectMapping()) {
            AbstractDirectMapping directMapping = (AbstractDirectMapping) mapping;

            directMapping.setFieldClassification(String.class);
        }
    }
}
