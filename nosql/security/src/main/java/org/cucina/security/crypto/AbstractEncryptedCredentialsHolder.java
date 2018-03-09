package org.cucina.security.crypto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class AbstractEncryptedCredentialsHolder {
	private PasswordDecryptor passwordDecryptor;
	private String decryptedPassword;
	private String password;
	private String username;

	/**
	 * Creates a new AbstractEncryptedCredentialsDataSource object.
	 *
	 * @param passwordDecryptor JAVADOC.
	 */
	public AbstractEncryptedCredentialsHolder(PasswordDecryptor passwordDecryptor) {
		super();
		Assert.notNull(passwordDecryptor, "passwordDecryptor is null");
		this.passwordDecryptor = passwordDecryptor;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param password JAVADOC.
	 */
	public void setPassword(String password) {
		this.password = password;
		this.decryptedPassword = null;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	protected String getUsername() {
		return username;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param username JAVADOC.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	protected String getDecryptedPassword() {
		if ((decryptedPassword == null) && StringUtils.isNotEmpty(password)) {
			decryptedPassword = passwordDecryptor.decrypt(password);
		}

		return decryptedPassword;
	}
}
