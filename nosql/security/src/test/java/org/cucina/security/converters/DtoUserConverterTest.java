package org.cucina.security.converters;

import org.cucina.security.api.UserDto;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * @author vlevine
 */
public class DtoUserConverterTest {
	private DtoUserConverter converter;
	@Mock
	private UserRepository repository;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		converter = new DtoUserConverter(repository);
	}

	/**
	 *
	 */
	@Test
	public void testConvert() {
		UserDto dto = new UserDto();

		dto.setUsername("name");
		dto.setPassword("P");
		dto.setEmail("@");

		User user = new User();

		when(repository.findByUsername(dto.getUsername())).thenReturn(user);

		User bean = converter.convert(dto);

		assertEquals("name", bean.getUsername());
		assertEquals("P", bean.getPassword().getValue());
		assertEquals("@", bean.getEmail());
	}
}
