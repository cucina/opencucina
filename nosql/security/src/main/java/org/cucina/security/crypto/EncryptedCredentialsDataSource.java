
package org.cucina.security.crypto;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EncryptedCredentialsDataSource
    extends AbstractEncryptedCredentialsHolder
    implements DataSource, InitializingBean {
    private DataSource dataSource;
    private boolean upset = false;

    /**
     * Creates a new EncryptedCredentialsDataSource object.
     */
    public EncryptedCredentialsDataSource(PasswordDecryptor passwordDecryptor, DataSource dataSource) {
        super(passwordDecryptor);
        Assert.notNull(dataSource, "dataSource is null");
        this.dataSource = dataSource;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     * @throws SQLException
     *
     * @throws JMSException
     *             JAVADOC.
     */
    @Override
    public Connection getConnection()
        throws SQLException {
        if (upset) {
            return dataSource.getConnection();
        }

        return dataSource.getConnection(getUsername(), getDecryptedPassword());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0
     *            JAVADOC.
     * @param arg1
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws JMSException
     *             JAVADOC.
     */
    @Override
    public Connection getConnection(String username, String password)
        throws SQLException {
        return dataSource.getConnection(username, password);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param out
     *            JAVADOC.
     *
     * @throws SQLException
     *             JAVADOC.
     */
    @Override
    public void setLogWriter(PrintWriter out)
        throws SQLException {
        dataSource.setLogWriter(out);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     *
     * @throws SQLException
     *             JAVADOC.
     */
    @Override
    public PrintWriter getLogWriter()
        throws SQLException {
        return dataSource.getLogWriter();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param seconds
     *            JAVADOC.
     *
     * @throws SQLException
     *             JAVADOC.
     */
    @Override
    public void setLoginTimeout(int seconds)
        throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     *
     * @throws SQLException
     *             JAVADOC.
     */
    @Override
    public int getLoginTimeout()
        throws SQLException {
        return dataSource.getLoginTimeout();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     *
     * @throws SQLFeatureNotSupportedException
     *             JAVADOC.
     */
    @Override
    public Logger getParentLogger()
        throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param iface
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws SQLException
     *             JAVADOC.
     */
    @Override
    public boolean isWrapperFor(Class<?> iface)
        throws SQLException {
        return dataSource.isWrapperFor(iface);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception
     *             JAVADOC.
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        BeanWrapper bw = new BeanWrapperImpl(dataSource);
        boolean uset = false;

        if (StringUtils.isNotEmpty(getUsername()) && bw.isWritableProperty("username")) {
            bw.setPropertyValue("username", getUsername());
            uset = true;
        }

        String pw = getDecryptedPassword();

        if (StringUtils.isNotEmpty(pw) && bw.isWritableProperty("password")) {
            bw.setPropertyValue("password", pw);

            if (uset) {
                upset = true;
            }
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param <T>
     *            JAVADOC.
     * @param iface
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws SQLException
     *             JAVADOC.
     */
    @Override
    public <T> T unwrap(Class<T> iface)
        throws SQLException {
        return dataSource.unwrap(iface);
    }
}
