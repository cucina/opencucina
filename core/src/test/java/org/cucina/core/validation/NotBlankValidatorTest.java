
package org.cucina.core.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class NotBlankValidatorTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testInitialize() {
        NotBlankValidator validator = new NotBlankValidator();

        validator.initialize(null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsValid() {
        NotBlankValidator validator = new NotBlankValidator();

        assertFalse(validator.isValid("", null));
        assertFalse(validator.isValid("  ", null));
        assertTrue(validator.isValid("hello", null));
        assertTrue(validator.isValid(null, null));
    }
}
