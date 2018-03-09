package org.cucina.security.converters;

import com.mongodb.DBObject;
import org.cucina.security.crypto.Encryptor;
import org.cucina.security.model.Password;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * @author vlevine
 */
public class PasswordWriteConverterTest {
	@Mock
	private Encryptor encryptor;
	private PasswordWriteConverter converter;

	/**
	 * @throws Exception .
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		converter = new PasswordWriteConverter(encryptor);
	}

	/**
	 *
	 */
	@Test
	public void testConvert() {
		Password source = new Password("value");

		when(encryptor.encrypt("value")).thenReturn("PASS");

		DBObject p = converter.convert(source);

		assertEquals("PASS", p.get("encrypted"));
	}
}
