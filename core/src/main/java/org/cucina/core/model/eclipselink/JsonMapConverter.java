
package org.cucina.core.model.eclipselink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Clob;
import java.util.Map;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.mappings.foundation.AbstractDirectMapping;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User type which utilises json to store/read a map structure.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JsonMapConverter
    implements Converter {
    private static final long serialVersionUID = -8777532729909911944L;
    private static final Logger LOG = LoggerFactory.getLogger(JsonMapConverter.class);

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

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting value to Map [" + val.toString() + "]");
        }

        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new StringReader(val.toString()));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return JsonValueExternFactory.fromString(sb.toString(), Map.class);
        } catch (IOException e) {
            LOG.error("Oops", e);
            throw new RuntimeException("IOException", e);
        }
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
    public Object convertObjectValueToDataValue(Object val, Session session) {
        if (!(val instanceof Map<?, ?>)) {
            LOG.debug("value [" + val + "] not an instance of Map");

            return null;
        }

        Map<?, ?> sb = (Map<?, ?>) val;

        return JsonValueExternFactory.toString(sb);
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

            directMapping.setFieldClassification(Clob.class);
        }
    }
}
