package org.cucina.security.crypto;

import javax.jms.JMSException;
import javax.jms.XAConnection;
import javax.jms.XAConnectionFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EncryptedCredentialsXAConnectionFactory
    extends AbstractEncryptedCredentialsHolder
    implements XAConnectionFactory {
    private XAConnectionFactory targetConnectionFactory;

    /**
     * Creates a new EncryptedCredentialsConnectionFactory object.
     */
    public EncryptedCredentialsXAConnectionFactory(PasswordDecryptor passwordDecryptor,
        XAConnectionFactory targetConnectionFactory) {
        super(passwordDecryptor);
        Assert.notNull(targetConnectionFactory, "targetConnectionFactory is null");
        this.targetConnectionFactory = targetConnectionFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     *
     * @throws JMSException JAVADOC.
     */
    @Override
    public XAConnection createXAConnection()
        throws JMSException {
        if (StringUtils.isNotEmpty(getUsername())) {
            return createXAConnection(getUsername(), getDecryptedPassword());
        }

        return targetConnectionFactory.createXAConnection();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0 JAVADOC.
     * @param arg1 JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws JMSException JAVADOC.
     */
    @Override
    public XAConnection createXAConnection(String username, String password)
        throws JMSException {
        return targetConnectionFactory.createXAConnection(username, password);
    }
}
