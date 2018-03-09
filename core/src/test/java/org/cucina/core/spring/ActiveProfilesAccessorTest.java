package org.cucina.core.spring;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ActiveProfilesAccessorTest {
	@Mock
	private Environment environment;

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
	@Test
	public void testGetActiveProfiles() {
		ActiveProfilesAccessor accessor = new ActiveProfilesAccessor();

		when(environment.getActiveProfiles()).thenReturn(new String[]{})
				.thenReturn(new String[]{"x", "y"});

		accessor.setEnvironment(environment);

		assertArrayEquals(new String[]{}, accessor.getActiveProfiles());
		assertArrayEquals(new String[]{"x", "y"}, accessor.getActiveProfiles());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetSsoProfile() {
		ActiveProfilesAccessor accessor = new ActiveProfilesAccessor();
		String key = "KEY";

		accessor.setSsoProfileKey(key);
		assertEquals(key, accessor.getSsoProfileKey());
	}
}
