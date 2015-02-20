package org.cucina.cluster.spring;

import org.apache.commons.lang3.StringUtils;
import org.cucina.cluster.event.ClusterControlEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ApplicationNameFilter
    implements MessageSelector, MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationNameFilter.class);
    private String applicationName;

    /**
     * Creates a new ValidatingClusterEventDelegate object.
     *
     * @param applicationName JAVADOC.
     */
    public ApplicationNameFilter(String applicationName) {
        Assert.notNull(applicationName);
        Assert.hasLength(applicationName);
        this.applicationName = applicationName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean accept(Message<?> message) {
        Object payloadObject = message.getPayload();

        if (payloadObject instanceof ClusterControlEvent) {
            ClusterControlEvent event = (ClusterControlEvent) payloadObject;

            if (StringUtils.isNotEmpty(event.getApplicationName()) &&
                    !event.getApplicationName().equals(applicationName)) {
                //wrong application name, return false
                if (LOG.isInfoEnabled()) {
                    LOG.info("Wrong application name " + event.getApplicationName() +
                        ". Correct one is" + applicationName);
                }

                return false;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("New event received. Publishing [" + event + "]");
            }
        } else {
            if (LOG.isInfoEnabled()) {
                LOG.info("Received something other than a clustercontrolevent over cluster [" +
                    payloadObject + "]");
            }
        }

        return true;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     *
     * @throws MessagingException JAVADOC.
     */
    @Override
    public void handleMessage(Message<?> message)
        throws MessagingException {
        LOG.warn("CHECK CONFIGURATION. " +
            "Received cluster message from a different application that has been configured with the same cluster.destination jms topic. " +
            "This is incorrect.  This message will be ignored. This application has application.name " +
            applicationName + ". Message [" + message + "]");
    }
}
