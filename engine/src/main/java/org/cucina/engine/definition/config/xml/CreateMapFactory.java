
package org.cucina.engine.definition.config.xml;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;


/**
 * Rule which creates a Map and sets it on the top item on the stack according to the 'name' attribute.
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class CreateMapFactory
    extends AbstractCreateObjectFactory<Map<Object, Object>> {
    /**
     * Creates new HashMap
     *
     * @return HashMap.
     */
    @Override
    public Map<Object, Object> createObjectImpl(Attributes attributes) {
        return new HashMap<Object, Object>();
    }
}
