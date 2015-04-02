package org.cucina.engine.client.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class OperativeFactoryImpl
    implements OperativeFactory, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(OperativeFactoryImpl.class);
    private ApplicationContext applicationContext;
    private Map<String, MessageChannel> channels = new HashMap<String, MessageChannel>();
    private String operativeName = "operative";

    /**
    * JAVADOC Method Level Comments
    *
    * @param applicationContext JAVADOC.
    *
    * @throws BeansException JAVADOC.
    */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param operativeName JAVADOC.
     */
    public void setOperativeName(String operativeName) {
        this.operativeName = operativeName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Operative createOperative(String conversationId) {
        // TODO have a pool
        QueueChannel queueChannel = new QueueChannel(10);

        if (LOG.isDebugEnabled()) {
            LOG.debug("For conversationId '" + conversationId + "' adding channel " + queueChannel);
        }

        channels.put(conversationId, queueChannel);

        OperativeImpl operative = applicationContext.getBean(operativeName, OperativeImpl.class);

        Assert.notNull(operative, "No bean " + operativeName + " found");

        operative.setReplyChannel(queueChannel);

        return operative;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public MessageChannel findChannel(String conversationId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Request for conversationId '" + conversationId + "' " + channels);
        }

        return channels.get(conversationId);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param operative JAVADOC.
     */
    @Override
    public void releaseChannel(String conversationId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Releasing conversation " + conversationId);
        }

        channels.remove(conversationId);
    }
}
