
package org.cucina.engine.definition.config.xml;

import org.xml.sax.Attributes;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PropertyCreationFactory
    extends AbstractCreateObjectFactory<String> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param attributes JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    protected String createObjectImpl(Attributes attributes) {
        return attributes.getValue("value");
    }
}
