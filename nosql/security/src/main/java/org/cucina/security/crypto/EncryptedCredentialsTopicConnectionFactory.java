package org.cucina.security.crypto;

import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class EncryptedCredentialsTopicConnectionFactory
    extends EncryptedCredentialsConnectionFactory
    implements TopicConnectionFactory {
    private TopicConnectionFactory targetConnectionFactory;

    /**
     * Creates a new EncryptedCredentialsTopicConnectionFactory object.
     *
     * @param passwordDecryptor JAVADOC.
     * @param targetConnectionFactory JAVADOC.
     */
    public EncryptedCredentialsTopicConnectionFactory(PasswordDecryptor passwordDecryptor,
        TopicConnectionFactory targetConnectionFactory) {
        super(passwordDecryptor, targetConnectionFactory);
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
    public TopicConnection createTopicConnection()
        throws JMSException {
        if (StringUtils.isNotEmpty(getUsername())) {
            return createTopicConnection(getUsername(), getDecryptedPassword());
        }

        return targetConnectionFactory.createTopicConnection();
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
    public TopicConnection createTopicConnection(String username, String password)
        throws JMSException {
        return targetConnectionFactory.createTopicConnection(username, password);
    }
}
