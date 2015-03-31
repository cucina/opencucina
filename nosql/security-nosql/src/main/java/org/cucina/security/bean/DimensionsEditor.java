package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;

import java.io.IOException;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.cucina.security.model.Dimension;


/**
 * Extension of PropertyEditorSupport to convert json dimension into Dimensions.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DimensionsEditor
    extends PropertyEditorSupport {
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Converts json dimensions into populated Dimension instances.
     *
     * @param text String csv role names.
     *
     * @throws IllegalArgumentException.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setAsText(String text)
        throws IllegalArgumentException {
        Collection<Dimension> value = new HashSet<Dimension>();

        if (StringUtils.isNotEmpty(text)) {
            try {
                Collection<Dimension> dims = objectMapper.readValue(text, Collection.class);

                if (dims == null) {
                    throw new IllegalArgumentException("Failed to deserialize");
                }

                value.addAll(dims);
            } catch (JsonParseException e) {
                throw new IllegalArgumentException(e);
            } catch (JsonMappingException e) {
                throw new IllegalArgumentException(e);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        setValue(value);
    }
}
