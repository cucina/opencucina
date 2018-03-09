package org.cucina.search;

import org.cucina.search.query.SearchBean;
import org.cucina.search.testassist.Foo;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class HasAttachmentsProjectionProviderTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testProvide() {
		HasAttachmentsProjectionProvider provider = new HasAttachmentsProjectionProvider();
		LinkedHashMap<String, String> aliasByType = new LinkedHashMap<String, String>();

		aliasByType.put(Foo.TYPE, "foo");

		SearchBean bean = new SearchBean();

		bean.setAliasByType(aliasByType);
		provider.provide(Foo.TYPE, bean);
		assertEquals("Incorrect number projections", 1, bean.getProjections().size());
		assertNotNull(bean.getProjection("hasAttachments"));
	}
}
