package org.cucina.search.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.cucina.core.model.eclipselink.AbstractJsonConverter;
import org.cucina.core.model.eclipselink.JsonMarshallerFactory;

import org.cucina.search.query.SearchBean;

import org.eclipse.persistence.sessions.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchBeanConverter
    extends AbstractJsonConverter {
    private static final long serialVersionUID = -8777532729909911944L;
    private static final Logger LOG = LoggerFactory.getLogger(SearchBeanConverter.class);

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
