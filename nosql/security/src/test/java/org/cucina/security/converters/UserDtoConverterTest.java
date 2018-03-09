package org.cucina.security.converters;

import org.cucina.security.api.UserDto;
import org.cucina.security.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;


/**
 * @author vlevine
 */
public class UserDtoConverterTest {
	private UserDtoConverter converter;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		converter = new UserDtoConverter();
	}

	/**
	 *
	 */
	@Test
	public void testConvert() {
		User u = new User();

		u.setUsername("name");
		u.setPassword("P");
		u.setEmail("@");
		u.setId(BigInteger.valueOf(111L));

		UserDto bean = converter.convert(u);

		assertEquals("name", bean.getUsername());
		assertEquals(111L, bean.getId().longValue());
		assertEquals("@", bean.getEmail());
	}
}
