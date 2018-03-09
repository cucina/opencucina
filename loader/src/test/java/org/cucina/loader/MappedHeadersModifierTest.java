package org.cucina.loader;

import org.cucina.loader.testassist.Foo;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MappedHeadersModifierTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testModifyHeaders() {
		Map<Class<?>, Map<String, String>> headersMap = new HashMap<Class<?>, Map<String, String>>();
		Map<String, String> maps = new HashMap<String, String>();

		maps.put("a", "name");
		maps.put("c", "value");
		headersMap.put(Foo.class, maps);

		MappedHeadersModifier modifier = new MappedHeadersModifier(headersMap);
		String[] result = modifier.modifyHeaders(new String[]{"a", "b", "c"}, Foo.class);

		assertEquals("name", result[0]);
		assertEquals(null, result[1]);
		assertEquals("value", result[2]);
	}
}
