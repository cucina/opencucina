package org.cucina.security.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
public class CurrentUserAccessorTest {
	@Mock
	private UserDetails user;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@After
	public void tearDown() {
		SecurityContext context = null;

		if (null == SecurityContextHolder.getContext()) {
			context = new SecurityContextImpl();

			SecurityContextHolder.setContext(context);
		}

		context = SecurityContextHolder.getContext();
		context.setAuthentication(null);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testCurrentAuthentication() {
		Authentication au = CurrentUserAccessor.currentAuthentication();

		assertNull("authentication should be null", au);
		setSecurityContext();
		au = CurrentUserAccessor.currentAuthentication();
		assertNotNull("authentication should not be null", au);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetCurrentUserDetails() {
		Object o = CurrentUserAccessor.getCurrentUserDetails();

		assertNull("details should be null", o);
		setSecurityContext();
		o = CurrentUserAccessor.getCurrentUserDetails();
		assertNotNull("details should not be null", o);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetCurrentUserName() {
		SecurityContextHolder.clearContext();
		assertNull(CurrentUserAccessor.getCurrentUserName());

		SecurityContext context = new SecurityContextImpl();

		SecurityContextHolder.setContext(context);
		assertNull(CurrentUserAccessor.getCurrentUserName());
		setSecurityContext();
		assertEquals("User", CurrentUserAccessor.getCurrentUserName());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testIsUserAuthenticated() {
		assertFalse("Oops, there should not be an authenticated user!",
				CurrentUserAccessor.isUserAuthenticated());
		setSecurityContext();
		assertTrue("Oops, there is no authenticated user!",
				CurrentUserAccessor.isUserAuthenticated());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testNoBarfWhenUserDetailsAreNotString() {
		SecurityContextHolder.clearContext();
		assertNull(CurrentUserAccessor.getCurrentUserDetails());

		setSecurityContext();

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		String details = "blaah";

		auth.setDetails(details);
		assertEquals(details, CurrentUserAccessor.getCurrentUserDetails());

		Object foo = new Object();

		auth.setDetails(foo);
		assertEquals(foo, CurrentUserAccessor.getCurrentUserDetails());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSwitchAuthentication() {
		setSecurityContext();

		Authentication au = CurrentUserAccessor.currentAuthentication();
		Authentication auth = mock(Authentication.class);
		Authentication oldau = CurrentUserAccessor.switchAuthentication(auth);

		assertEquals(au, oldau);
		au = CurrentUserAccessor.currentAuthentication();

		assertEquals(auth, au);
	}

	private void setSecurityContext() {
		SecurityContext context = SecurityContextHolder.getContext();

		when(user.getUsername()).thenReturn("User");

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
				null, null);

		authToken.setDetails(new Object());
		context.setAuthentication(authToken);
	}
}
