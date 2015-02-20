package org.cucina.security.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.security.repository.PrivilegeRepository;
import org.junit.Before;
import org.junit.Test;


/**
 * Validates that UniquePrivilegeValidator functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniquePrivilegeValidatorTest {
    private static final String PRIVILEGE_NAME = "myPrivilege";
    private UniquePrivilegeValidator validator;

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void callInitialize() {
        validator.initialize(null);
    }

    /**
     * Sets up for test
     */
    @Before
    public void onsetup() {
        validator = new UniquePrivilegeValidator();
    }

    /**
     * Tests that if privilege does exist fails validation
     */
    @Test
    public void testFailsValidation() {
        PrivilegeRepository privilegeRepository = mock(PrivilegeRepository.class);

        when(privilegeRepository.exists(PRIVILEGE_NAME)).thenReturn(true);
        validator.setPrivilegeRepository(privilegeRepository);
        assertFalse("Should have failed validation", validator.isValid(PRIVILEGE_NAME, null));
    }

    /**
     * Tests that if privilege doesn't exist is valid
     */
    @Test
    public void testValidates() {
        PrivilegeRepository privilegeRepository = mock(PrivilegeRepository.class);

        when(privilegeRepository.exists(PRIVILEGE_NAME)).thenReturn(false);
        validator.setPrivilegeRepository(privilegeRepository);
        assertTrue("Should have returned true", validator.isValid(PRIVILEGE_NAME, null));
    }
}
