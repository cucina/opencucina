package org.cucina.core.model.eclipselink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import java.util.Map;

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
    extends AbstractJsonConverter {
    private static final long serialVersionUID = -8777532729909911944L;
    private static final Logger LOG = LoggerFactory.getLogger(JsonMapConverter.class);

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

            return JsonMarshallerFactory.fromString(sb.toString(), Map.class);
        } catch (IOException e) {
            LOG.error("Oops", e);
            throw new RuntimeException("IOException", e);
        }
    }
}
