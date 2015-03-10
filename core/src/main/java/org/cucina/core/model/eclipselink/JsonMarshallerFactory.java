package org.cucina.core.model.eclipselink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.lang3.StringUtils;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.core.spring.SingletonBeanFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Requires for the bean of type {@link JacksonMarshaller} to be present in the
 * Spring context.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JsonMarshallerFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JsonMarshallerFactory.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param vm
     *            JAVADOC.
     * @param val
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static <T> T fromString(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new StringReader(str));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return getMarshaller().unmarshall(sb.toString(), clazz);
        } catch (IOException e) {
            LOG.error("Oops", e);
            throw new RuntimeException("IOException", e);
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param vm
     *            JAVADOC.
     * @param val
     *            JAVADOC.
     * @param store
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public static String toString(Object object) {
        if (object != null) {
            return getMarshaller().marshall(object);
        }

        return null;
    }

    private static Marshaller getMarshaller() {
        return SingletonBeanFactory.getInstance().getBean(JacksonMarshaller.class);
    }
}
