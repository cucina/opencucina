package org.cucina.security.crypto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.jms.JMSException;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class EncryptedCredentialsXADataSource extends AbstractEncryptedCredentialsHolder
		implements XADataSource {
	private XADataSource dataSource;

	/**
	 * Creates a new EncryptedCredentialsDataSource object.
	 */
	public EncryptedCredentialsXADataSource(PasswordDecryptor passwordDecryptor, XADataSource dataSource) {
		super(passwordDecryptor);
		Assert.notNull(dataSource, "dataSource is null");
		this.dataSource = dataSource;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 * @throws SQLException
	 * @throws JMSException JAVADOC.
	 */
	@Override
	public XAConnection getXAConnection()
			throws SQLException {
		if (StringUtils.isNotEmpty(getUsername())) {
			return getXAConnection(getUsername(), getDecryptedPassword());
		}
		return dataSource.getXAConnection();
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
	public XAConnection getXAConnection(String username, String password)
			throws SQLException {
		return dataSource.getXAConnection(username, password);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 * @throws SQLException JAVADOC.
	 */
	@Override
	public PrintWriter getLogWriter()
			throws SQLException {
		return dataSource.getLogWriter();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param out JAVADOC.
	 * @throws SQLException JAVADOC.
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
	 * @throws SQLException JAVADOC.
	 */
	@Override
	public int getLoginTimeout()
			throws SQLException {
		return dataSource.getLoginTimeout();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param seconds JAVADOC.
	 * @throws SQLException JAVADOC.
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
	 * @throws SQLFeatureNotSupportedException JAVADOC.
	 */
	@Override
	public Logger getParentLogger()
			throws SQLFeatureNotSupportedException {
		return dataSource.getParentLogger();
	}
}
