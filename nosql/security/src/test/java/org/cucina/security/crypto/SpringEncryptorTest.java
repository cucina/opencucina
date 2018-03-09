package org.cucina.security.crypto;

import org.springframework.security.crypto.keygen.KeyGenerators;

import static org.junit.Assert.assertEquals;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SpringEncryptorTest {
	private SpringEncryptor encryptor;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	//@Before
	public void setUp()
			throws Exception {
		System.err.println(KeyGenerators.string().generateKey());
		encryptor = new SpringEncryptor("password");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	//@Test
	public void testDecrypt() {
		String ec = encryptor.encrypt("textABC");

		assertEquals("textABC", encryptor.decrypt(ec));
	}
}
