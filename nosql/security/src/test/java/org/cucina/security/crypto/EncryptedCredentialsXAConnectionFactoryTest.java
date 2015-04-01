
package org.cucina.security.crypto;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.XAConnection;
import javax.jms.XAConnectionFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EncryptedCredentialsXAConnectionFactoryTest {
    @Mock
    private XAConnection connection;
    @Mock
    private XAConnection userPwdConnection;
    private EncryptedCredentialsXAConnectionFactory connectionFactory;
    @Mock
    private PasswordDecryptor passwordDecryptor;
    @Mock
    private XAConnectionFactory delegate;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        connectionFactory = new EncryptedCredentialsXAConnectionFactory(passwordDecryptor, delegate);
        when(connectionFactory.createXAConnection(anyString(), anyString()))
        .thenReturn(userPwdConnection);
        when(connectionFactory.createXAConnection()).thenReturn(connection);
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

        assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createXAConnection());
        verify(delegate).createXAConnection("username", "pvalue");
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

        assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createXAConnection());
        assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createXAConnection());
        verify(passwordDecryptor, times(1)).decrypt("password");
        verify(delegate, times(2)).createXAConnection("username", "pvalue");

        connectionFactory.setPassword("newPassword");
        when(passwordDecryptor.decrypt("newPassword")).thenReturn("newpvalue");
        assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createXAConnection());
        verify(passwordDecryptor, times(1)).decrypt("newPassword");
        verify(delegate, times(1)).createXAConnection("username", "newpvalue");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetXAConnection()
        throws Exception {
    	assertEquals("Incorrect connection", connection, connectionFactory.createXAConnection());
        verify(delegate).createXAConnection();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetConnectionStringString()
        throws Exception {
    	assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createXAConnection("u", "p"));
        verify(delegate).createXAConnection("u", "p");
    }
}
