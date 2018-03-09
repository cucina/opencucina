package org.cucina.security.converters;

import com.mongodb.DBObject;
import org.cucina.security.crypto.Encryptor;
import org.cucina.security.model.Password;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author vlevine
 */
public class PasswordReadConverterTest {
	@Mock
	private Encryptor encryptor;
	private PasswordReadConverter converter;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		converter = new PasswordReadConverter(encryptor);
	}

	/**
	 *
	 */
	@Test
	public void testConvert() {
		DBObject source = mock(DBObject.class);

		when(source.get("encrypted")).thenReturn("ENC");
		when(encryptor.decrypt("ENC")).thenReturn("PASS");

		Password p = converter.convert(source);

		assertEquals("PASS", p.getValue());
	}
}
