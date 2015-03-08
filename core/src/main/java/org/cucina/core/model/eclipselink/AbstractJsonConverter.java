package org.cucina.core.model.eclipselink;

import java.sql.Clob;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.mappings.foundation.AbstractDirectMapping;
import org.eclipse.persistence.sessions.Session;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public abstract class AbstractJsonConverter
    implements Converter {
    private static final long serialVersionUID = -8716825624668028329L;

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
    public Object convertObjectValueToDataValue(Object val, Session session) {
        return JsonMarshallerFactory.toString(val);
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
