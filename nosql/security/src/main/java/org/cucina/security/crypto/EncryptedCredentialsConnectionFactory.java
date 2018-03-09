package org.cucina.security.crypto;


import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EncryptedCredentialsConnectionFactory
		extends AbstractEncryptedCredentialsHolder
		implements ConnectionFactory {
	private ConnectionFactory targetConnectionFactory;

	/**
	 * Creates a new EncryptedCredentialsConnectionFactory object.
	 */
	public EncryptedCredentialsConnectionFactory(PasswordDecryptor passwordDecryptor,
												 ConnectionFactory targetConnectionFactory) {
		super(passwordDecryptor);
		Assert.notNull(targetConnectionFactory, "targetConnectionFactory is null");
		this.targetConnectionFactory = targetConnectionFactory;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 * @throws JMSException JAVADOC.
	 */
	@Override
	public Connection createConnection()
			throws JMSException {
		if (StringUtils.isNotEmpty(getUsername())) {
			return createConnection(getUsername(), getDecryptedPassword());
		}

		return targetConnectionFactory.createConnection();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param arg0 JAVADOC.
	 * @param arg1 JAVADOC.
	 * @return JAVADOC.
	 * @throws JMSException JAVADOC.
	 */
	@Override
	public Connection createConnection(String username, String password)
			throws JMSException {
		return targetConnectionFactory.createConnection(username, password);
	}
}
