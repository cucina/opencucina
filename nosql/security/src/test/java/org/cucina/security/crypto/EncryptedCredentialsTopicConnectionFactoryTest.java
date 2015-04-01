package org.cucina.security.crypto;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EncryptedCredentialsTopicConnectionFactoryTest {

    @Mock
    private TopicConnection connection;
    @Mock
    private TopicConnection userPwdConnection;
    @Mock
    private TopicConnectionFactory delegate;
    private EncryptedCredentialsTopicConnectionFactory connectionFactory;
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
        connectionFactory = new EncryptedCredentialsTopicConnectionFactory(passwordDecryptor, delegate);
        when(connectionFactory.createTopicConnection(anyString(), anyString()))
            .thenReturn(userPwdConnection);
        when(connectionFactory.createTopicConnection()).thenReturn(connection);
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

        assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createTopicConnection());
        verify(delegate).createTopicConnection("username", "pvalue");
        verify(delegate, never()).createTopicConnection();
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

        assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createTopicConnection());
        assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createTopicConnection());
        verify(passwordDecryptor, times(1)).decrypt("password");
        verify(delegate, times(2)).createTopicConnection("username", "pvalue");

        connectionFactory.setPassword("newPassword");
        when(passwordDecryptor.decrypt("newPassword")).thenReturn("newpvalue");
        assertEquals("Incorrect connection", userPwdConnection, connectionFactory.createTopicConnection());
        verify(passwordDecryptor, times(1)).decrypt("newPassword");
        verify(delegate, times(1)).createTopicConnection("username", "newpvalue");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetXAConnection()
        throws Exception {
        assertEquals("Incorrect connection", connection, connectionFactory.createTopicConnection());
        verify(delegate).createTopicConnection();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetConnectionStringString()
        throws Exception {
        assertEquals("Incorrect connection", userPwdConnection,
            connectionFactory.createTopicConnection("u", "p"));
        verify(delegate).createTopicConnection("u", "p");
    }
}
