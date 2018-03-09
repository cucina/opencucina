package org.cucina.security.crypto;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface Encryptor {
	//String SALT = "616c676f7a657374";
	String SALT = "dac7daf24bbe63c9";

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param text JAVADOC.
	 * @return JAVADOC.
	 * @throws UnsupportedEncodingException
	 * @throws GeneralSecurityException
	 */
	String decrypt(String text);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param text JAVADOC.
	 * @return JAVADOC.
	 * @throws UnsupportedEncodingException
	 * @throws GeneralSecurityException
	 */
	String encrypt(String text);
}
