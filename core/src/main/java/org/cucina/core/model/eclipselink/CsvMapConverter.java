
package org.cucina.core.model.eclipselink;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.mappings.foundation.AbstractDirectMapping;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of Strategy annotation as well as Externalizer/Factory
 * annotations. Only one of these should be used.
 *
 * for <code>@Strategy</code> use this
 * <pre>
 *         @Persistent
 *         @Strategy(value = "org.cucina.meringue.model.internal.CsvMapValueHandler")
 * </pre>
 *
 *        for <code>@Externalizer/@Factory</code>
 *
 *        <pre>
 *                @Persistent
 *                 @Externalizer(value = "org.cucina.meringue.model.internal.CsvMapValueHandler.toString")
 *                 @Factory(value = "org.cucina.meringue.model.internal.CsvMapValueHandler.fromString")
 *  </pre>
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvMapConverter
    implements Converter {
    private static final long serialVersionUID = -4544057848189055214L;
    private static final Logger LOG = LoggerFactory.getLogger(CsvMapConverter.class);

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

        if (!(val instanceof Map<?, ?>)) {
            return "Not a Map";
        }

        return toString((Map<String, String>) val);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param string
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static Map<String, String> fromString(String string) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deserialise value [" + string + "]");
        }

        Map<String, String> result = new HashMap<String, String>();
        String[] pairs = string.toString().split(",");

        for (int i = 0; i < pairs.length; i++) {
            String[] parts = pairs[i].trim().split("=");

            if (parts.length == 0) {
                continue;
            }

            if (parts.length == 1) {
                result.put(parts[0].trim(), null);
            } else {
                result.put(parts[0].trim(), parts[1].trim());
            }
        }

        return result;
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

    /**
    * JAVADOC Method Level Comments
    *
    * @param map
    *            JAVADOC.
    *
    * @return JAVADOC.
    */
    public static String toString(Map<String, String> map) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
        }

        String serString = sb.substring(0, sb.length() - 1);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Serialized value [" + serString + "]");
        }

        return serString;
    }
}
