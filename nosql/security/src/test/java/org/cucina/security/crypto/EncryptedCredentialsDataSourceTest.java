
package org.cucina.security.crypto;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
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
public class EncryptedCredentialsDataSourceTest {
    @Mock
    private BasicDataSource delegate;
    private EncryptedCredentialsDataSource dataSource;
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
        dataSource = new EncryptedCredentialsDataSource(passwordDecryptor, delegate);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAfterPropertiesSet()
        throws Exception {
        dataSource.setPassword("password");
        dataSource.setUsername("username");
        when(passwordDecryptor.decrypt("password")).thenReturn("pvalue");
        dataSource.afterPropertiesSet();
        verify(delegate).setUsername("username");
        verify(delegate).setPassword("pvalue");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testGetConnectionNonWritable()
        throws Exception {
        DataSource ds = mock(DataSource.class);

        dataSource = new EncryptedCredentialsDataSource(passwordDecryptor, ds);
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        when(passwordDecryptor.decrypt("password")).thenReturn("pvalue");
        dataSource.afterPropertiesSet();
        dataSource.getConnection();
        verify(ds).getConnection("username", "pvalue");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetConnectionStringString()
        throws Exception {
        dataSource.getConnection("u", "p");
        verify(delegate).getConnection("u", "p");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetConnectionWritable()
        throws Exception {
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        when(passwordDecryptor.decrypt("password")).thenReturn("pvalue");
        dataSource.afterPropertiesSet();
        dataSource.getConnection();
        verify(delegate).getConnection();
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
    public void testIsWrapperFor()
        throws Exception {
        dataSource.isWrapperFor(String.class);
        verify(delegate).isWrapperFor(String.class);
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

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testUnwrap()
        throws Exception {
        dataSource.unwrap(Object.class);
        verify(delegate).unwrap(Object.class);
    }
}
