package org.cucina.security.validation;

import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


/**
 * Tests that UserDaoUsernameValidatingPlugin functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class UserDetailServiceUsernameValidatingPluginTest {
	private static final String USERNAME = "username";
	private User user;
	@Mock
	private UserRepository userRepository;
	private UserRepositoryUsernameValidatingPlugin plugin;

	/**
	 * Message is incorrect.
	 */
	@Test
	public void message() {
		assertEquals("Should have correct message",
				"{org.cucina.security.validation.ValidUsername.unique}", plugin.message());
	}

	/**
	 * Sets up for test
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		plugin = new UserRepositoryUsernameValidatingPlugin(userRepository);

		user = new User();
	}

	/**
	 * User not found exception confirms username is valid
	 */
	@Test
	public void userNotFoundException() {
		when(userRepository.findByUsername(USERNAME)).thenReturn(null);
		assertTrue("Should be found as no user", plugin.isValid(USERNAME));
	}

	/**
	 * Username is invalid if user is found.
	 */
	@Test
	public void usernameFound() {
		when(userRepository.findByUsername(USERNAME)).thenReturn(user);

		assertFalse("Should not be valid as user is found", plugin.isValid(USERNAME));
	}

	/**
	 * Username is valid if no user is found
	 */
	@Test
	public void usernameNotFound() {
		assertTrue("Should be found as no user", plugin.isValid(USERNAME));
	}
}
