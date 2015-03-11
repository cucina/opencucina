package org.cucina.core.model.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User type which utilises json to store/read a map structure.
 *
 * @author $Author: $
 * @version $Revision: $
 */
@SuppressWarnings("rawtypes")
@Converter
public class JsonMapConverter
    implements AttributeConverter<Map, String> {
    private static final Logger LOG = LoggerFactory.getLogger(JsonMapConverter.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param val JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String convertToDatabaseColumn(Map val) {
        return JsonMarshallerFactory.toString(val);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param val JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Map convertToEntityAttribute(String val) {
        if (val == null) {
            return null;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting value to Map [" + val + "]");
        }

        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new StringReader(val));
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
