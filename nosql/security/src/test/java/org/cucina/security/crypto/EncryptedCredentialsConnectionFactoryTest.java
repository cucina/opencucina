package org.cucina.security.crypto;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EncryptedCredentialsConnectionFactoryTest {
	@Mock
	private Connection connection;
	@Mock
	private Connection userPwdConnection;
	@Mock
	private ConnectionFactory delegate;
	private EncryptedCredentialsConnectionFactory connectionFactory;
	@Mock
	private PasswordDecryptor passwordDecryptor;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		connectionFactory = new EncryptedCredentialsConnectionFactory(passwordDecryptor, delegate);
		when(connectionFactory.createConnection(anyString(), anyString()))
				.thenReturn(userPwdConnection);
		when(connectionFactory.createConnection()).thenReturn(connection);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetConnectionUsernameSet()
			throws Exception {
		connectionFactory.setPassword("password");
		connectionFactory.setUsername("username");
		when(passwordDecryptor.decrypt("password")).thenReturn("pvalue");

		assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createConnection());
		verify(delegate).createConnection("username", "pvalue");
		verify(delegate, never()).createConnection();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testDecryptPasswordAndCaches()
			throws Exception {
		connectionFactory.setPassword("password");
		connectionFactory.setUsername("username");
		when(passwordDecryptor.decrypt("password")).thenReturn("pvalue");

		assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createConnection());
		assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createConnection());
		verify(passwordDecryptor, times(1)).decrypt("password");
		verify(delegate, times(2)).createConnection("username", "pvalue");

		connectionFactory.setPassword("newPassword");
		when(passwordDecryptor.decrypt("newPassword")).thenReturn("newpvalue");
		assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createConnection());
		verify(passwordDecryptor, times(1)).decrypt("newPassword");
		verify(delegate, times(1)).createConnection("username", "newpvalue");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetXAConnection()
			throws Exception {
		assertEquals("Incorrect connection", connection, connectionFactory.createConnection());
		verify(delegate).createConnection();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetConnectionStringString()
			throws Exception {
		assertEquals("Incorrect connection", userPwdConnection,
				connectionFactory.createConnection("u", "p"));
		verify(delegate).createConnection("u", "p");
	}
}
