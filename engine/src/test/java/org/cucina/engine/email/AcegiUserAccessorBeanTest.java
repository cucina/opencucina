package org.cucina.engine.email;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Test that AcegiengineUserAccessor functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class AcegiUserAccessorBeanTest {
	private AcegiUserAccessorBean engineUserAccessor;

	/**
	 * Set up for test
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		engineUserAccessor = new AcegiUserAccessorBean();
	}

	/**
	 * Test current User is returned from Acegi
	 */
	@Test
	public void testGetCurrentUser() {
		setSecurityContext();

		Object found = engineUserAccessor.getCurrentUser();

		assertNotNull("No user found", found);
		assertEquals("UsEr", found);
	}

	private void setSecurityContext() {
		SecurityContext context = null;

		if (null == SecurityContextHolder.getContext()) {
			context = new SecurityContextImpl();

			SecurityContextHolder.setContext(context);
		}

		context = SecurityContextHolder.getContext();

		Authentication auth = mock(Authentication.class);

		when(auth.getName()).thenReturn("UsEr");
		context.setAuthentication(auth);
	}
}
