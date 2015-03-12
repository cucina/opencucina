package org.cucina.engine.checks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BooleanPropertyCheckTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testTest() {
        BooleanPropertyCheck check = new BooleanPropertyCheck();

        assertFalse(check.test(null));
        assertFalse(check.test(null));
        check.setTrue(true);
        assertTrue(check.test(null));
    }
}
