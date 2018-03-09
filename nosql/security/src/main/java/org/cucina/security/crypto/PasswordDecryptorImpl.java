package org.cucina.security.crypto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PasswordDecryptorImpl
		implements PasswordDecryptor {
	private Encryptor encryptor;
	private boolean crypt;

	/**
	 * Creates a new PasswordDecryptorImpl object.
	 *
	 * @param encryptor JAVADOC.
	 */
	public PasswordDecryptorImpl(Encryptor encryptor) {
		Assert.notNull(encryptor, "encryptor is null");
		this.encryptor = encryptor;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param crypt JAVADOC.
	 */
	public void setCrypt(boolean crypt) {
		this.crypt = crypt;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param password JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public String decrypt(String password) {
		if (!crypt) {
			return password;
		}

		return StringUtils.isEmpty(password) ? null : encryptor.decrypt(password);
	}
}
