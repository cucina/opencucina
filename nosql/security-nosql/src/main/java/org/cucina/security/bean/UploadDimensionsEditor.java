package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.cucina.security.model.Dimension;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UploadDimensionsEditor
    extends PropertyEditorSupport {
    private static final String TRAILING_ID_BRACKET = ")";
    private static final String TYPE_TO_ID_DELIMITER = "\\(";
    private static final String PROPERTY_TO_VALUE_DELIMITER = "=";
    private static final String DIMENSION_DELIMITER = ",";

    /**
     * JAVADOC Method Level Comments
     *
     * @param text JAVADOC.
     *
     * @throws IllegalArgumentException JAVADOC.
     */
    @Override
    public void setAsText(String text)
        throws IllegalArgumentException {
        //this must be a list as a hashset will result in problems due to 
        //equals implementation on PersistableObject (which is based on id which is not set)
        List<Dimension> ret = new ArrayList<Dimension>();

        if (StringUtils.isEmpty(text)) {
            setValue(ret);

            return;
        }

        String[] tokens = text.split(DIMENSION_DELIMITER);

        //each one has the form propertyName=type(id)
        for (String token : tokens) {
            String[] props = token.split(PROPERTY_TO_VALUE_DELIMITER);

            //should be token #1 = "propertyName"
            //should be token #2 = "type(id)"
            if (props.length == 2) {
                Dimension dim = new Dimension();

                dim.setPropertyName(props[0]);

                String[] typeId = props[1].split(TYPE_TO_ID_DELIMITER);

                //should be token #1 = "type"
                //should be token #2 = "id)"
                if (typeId.length == 2) {
                    dim.setDomainObjectType(typeId[0]);

                    String idStr = typeId[1];

                    if (typeId[1].endsWith(TRAILING_ID_BRACKET)) {
                        idStr = idStr.substring(0, idStr.length() - 1);
                    }

                    try {
                        BigInteger id = BigInteger.valueOf(Long.parseLong(idStr));

                        dim.setDomainObjectId(id);
                        ret.add(dim);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        }

        setValue(ret);
    }
}
