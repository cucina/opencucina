package org.cucina.search.query.projection;

import org.cucina.search.testassist.Foo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class HasAttachmentsProjectionTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void test() {
		HasAttachmentsProjection projection = new HasAttachmentsProjection("bla", Foo.TYPE);

		assertEquals(
				"(select (case count(history.id) when 0 then false else true end) from HistoryRecord history " +
						"where history.attachment is not null " +
						"and history.token.domainObjectType = 'Foo' " +
						"and history.token.domainObjectId = bla.id ) " + "as hasAttachments",
				projection.getProjection());
		assertFalse("Should not be groupable", projection.isGroupable());
	}
}
