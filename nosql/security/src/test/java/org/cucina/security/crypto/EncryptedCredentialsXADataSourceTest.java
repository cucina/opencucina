package org.cucina.security.crypto;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.XADataSource;

import static org.mockito.Mockito.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EncryptedCredentialsXADataSourceTest {
	@Mock
	private XADataSource delegate;
	private EncryptedCredentialsXADataSource dataSource;
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
		dataSource = new EncryptedCredentialsXADataSource(passwordDecryptor, delegate);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetConnectionUsernameSet()
			throws Exception {
		dataSource.setPassword("password");
		dataSource.setUsername("username");
		when(passwordDecryptor.decrypt("password")).thenReturn("pvalue");

		dataSource.getXAConnection();
		verify(delegate).getXAConnection("username", "pvalue");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testDecryptPasswordAndCaches()
			throws Exception {
		dataSource.setPassword("password");
		dataSource.setUsername("username");
		when(passwordDecryptor.decrypt("password")).thenReturn("pvalue");

		dataSource.getXAConnection();
		dataSource.getXAConnection();
		verify(passwordDecryptor, times(1)).decrypt("password");
		verify(delegate, times(2)).getXAConnection("username", "pvalue");

		dataSource.setPassword("newPassword");
		when(passwordDecryptor.decrypt("newPassword")).thenReturn("newpvalue");
		dataSource.getXAConnection();
		verify(passwordDecryptor, times(1)).decrypt("newPassword");
		verify(delegate, times(1)).getXAConnection("username", "newpvalue");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetXAConnection()
			throws Exception {
		dataSource.getXAConnection();
		verify(delegate).getXAConnection();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetConnectionStringString()
			throws Exception {
		dataSource.getXAConnection("u", "p");
		verify(delegate).getXAConnection("u", "p");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetLogWriter()
			throws Exception {
		dataSource.getLogWriter();
		verify(delegate).getLogWriter();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetLoginTimeout()
			throws Exception {
		dataSource.getLoginTimeout();
		verify(delegate).getLoginTimeout();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetParentLogger()
			throws Exception {
		dataSource.getParentLogger();
		verify(delegate).getParentLogger();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSetLogWriter()
			throws Exception {
		dataSource.setLogWriter(null);
		verify(delegate).setLogWriter(null);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSetLoginTimeout()
			throws Exception {
		dataSource.setLoginTimeout(0);
		verify(delegate).setLoginTimeout(0);
	}
}
