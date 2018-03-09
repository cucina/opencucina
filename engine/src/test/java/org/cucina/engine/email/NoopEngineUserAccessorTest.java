package org.cucina.engine.email;

import org.junit.Test;

import static org.junit.Assert.assertNull;


/**
 * Checks all nulls are beautiful.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NoopEngineUserAccessorTest {
	/**
	 * Null is the word
	 */
	@Test
	public void returnsNull() {
		NoopEngineUserAccessor accessor = new NoopEngineUserAccessor();

		assertNull("Should return null", accessor.getCurrentUser());
	}
}
