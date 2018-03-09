package org.cucina.security.validation;

import org.cucina.security.repository.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


/**
 * Validates that UniquePrivilegeValidatorTest.java functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class UniqueRoleValidatorTest {
	private static final String ROLE_NAME = "myRole";
	@Mock
	private RoleRepository roleRepository;
	private UniqueRoleValidator validator;

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
		MockitoAnnotations.initMocks(this);
		validator = new UniqueRoleValidator();
		validator.setRoleRepository(roleRepository);
	}

	/**
	 * Tests that if role does exist fails validation
	 */
	@Test
	public void testFailsValidation() {
		when(roleRepository.countByName(ROLE_NAME)).thenReturn(1);

		assertFalse("Should have failed validation", validator.isValid(ROLE_NAME, null));
	}

	/**
	 * Tests that if role doesn't exist is valid
	 */
	@Test
	public void testValidates() {
		when(roleRepository.countByName(ROLE_NAME)).thenReturn(0);
		assertTrue("Should have returned true", validator.isValid(ROLE_NAME, null));
	}
}
