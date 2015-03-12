
package org.cucina.engine.email;

import static org.junit.Assert.assertNull;

import org.junit.Test;


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
