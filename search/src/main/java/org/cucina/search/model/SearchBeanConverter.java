package org.cucina.search.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.persistence.AttributeConverter;

import org.cucina.core.model.support.JsonMarshallerFactory;
import org.cucina.search.query.SearchBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchBeanConverter
    implements AttributeConverter<SearchBean, String> {
    private static final Logger LOG = LoggerFactory.getLogger(SearchBeanConverter.class);

    /**
     * JAVADOC Method Level Comments
     *
     * @param attribute JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public String convertToDatabaseColumn(SearchBean attribute) {
        return JsonMarshallerFactory.toString(attribute);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param val JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public SearchBean convertToEntityAttribute(String val) {
        if (val == null) {
            return null;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Converting value to SearchBean [" + val.toString() + "]");
        }

        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new StringReader(val.toString()));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return JsonMarshallerFactory.fromString(sb.toString(), SearchBean.class);
        } catch (IOException e) {
            LOG.error("Oops", e);
            throw new RuntimeException("IOException", e);
        }
    }
}
