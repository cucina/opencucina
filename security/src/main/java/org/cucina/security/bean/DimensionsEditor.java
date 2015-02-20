package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.cucina.core.marshal.Marshaller;
import org.cucina.security.model.Dimension;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;


/**
 * Extension of PropertyEditorSupport to convert json dimension into Dimensions.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DimensionsEditor
    extends PropertyEditorSupport {
    private Marshaller marshaller;

    /**
         * Creates a new RolesEditor object.
         *
         * @param rolePrivilegeDao JAVADOC.
         */
    public DimensionsEditor(Marshaller marshaller) {
        Assert.notNull(marshaller, "marshaller cannot be null");
        this.marshaller = marshaller;
    }

    /**
     * Converts json dimensions into populated Dimension instances.
     *
     * @param text String csv role names.
     *
     * @throws IllegalArgumentException.
     */
    @Override
    public void setAsText(String text)
        throws IllegalArgumentException {
        Collection<Dimension> value = new HashSet<Dimension>();

        if (StringUtils.isNotEmpty(text)) {
            value.addAll(marshaller.unmarshall(text,
                    new TypeReference<Collection<Dimension>>() {
                }));
        }

        setValue(value);
    }
}
