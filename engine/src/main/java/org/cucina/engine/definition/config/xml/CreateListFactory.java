package org.cucina.engine.definition.config.xml;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;


/**
 * Rule which creates a List and sets it on the top item on the stack according to the 'name' attribute.
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class CreateListFactory
		extends AbstractCreateObjectFactory<List<Object>> {
	/**
	 * Creates and returns new ArrayList
	 *
	 * @return ArrayList<Object>.
	 */
	@Override
	public List<Object> createObjectImpl(Attributes attributes) {
		return new ArrayList<Object>();
	}
}
