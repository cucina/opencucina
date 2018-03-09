package org.cucina.security.authentication;

import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


/**
 * @author vlevine
 */
public class RepositoryAuthenticationServiceTest {
	private RepositoryAuthenticationService service;
	@Mock
	private UserRepository userRepository;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		service = new RepositoryAuthenticationService();
		ReflectionTestUtils.setField(service, "userRepository", userRepository);
	}

	/**
	 *
	 */
	@Test
	public void testAuthenticate() {
		User user = new User();
		user.setPassword("password");
		when(userRepository.findByUsername("username")).thenReturn(user);
		assertNotNull(service.authenticate("username", "password"));
	}
}
