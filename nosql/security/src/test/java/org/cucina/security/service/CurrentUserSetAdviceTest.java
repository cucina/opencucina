package org.cucina.security.service;

import org.cucina.security.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CurrentUserSetAdviceTest {
	private CurrentUserSetAdvice interceptor;
	@Mock
	private SystemUserService systemUserService;
	@Mock
	private UserAccessor userAccessor;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		clearSecurity();
		interceptor = new CurrentUserSetAdvice(systemUserService, userAccessor);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Throwable JAVADOC.
	 */
	@Test
	public void testInvokeInContext()
			throws Throwable {
		when(systemUserService.getUsername()).thenReturn("userName");

		Method method = this.getClass().getMethod("setup", (Class<?>[]) null);

		interceptor.before(method, null, null);

		verify(userAccessor).forceUserToContext("userName");
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Throwable JAVADOC.
	 */
	@Test
	public void testWithCurrentAuth()
			throws Throwable {
		setSecurity();

		interceptor.before(null, null, null);
		verify(systemUserService, never()).getUsername();
		verify(userAccessor, never()).forceUserToContext(anyString());
	}

	private void setSecurity() {
		SecurityContext context = SecurityContextHolder.getContext();
		User user = new User();

		user.setUsername("User");

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
				null, null);

		authToken.setDetails("detailsToken");
		context.setAuthentication(authToken);
	}

	private void clearSecurity() {
		SecurityContext context = SecurityContextHolder.getContext();

		context.setAuthentication(null);
	}
}
