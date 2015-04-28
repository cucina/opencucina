package org.cucina.engine.client.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This operative acts as a client to a remote server, possibly initiating a
 * conversation.
 *
 * @author vlevine
 */
public class ActiveOperative
    extends AbstractOperative {
    private static final Logger LOG = LoggerFactory.getLogger(ActiveOperative.class);

    // to forward request to the server
    private MessageChannel requestChannel;

    /**
     * Creates a new OperativeImpl object.
     *
     * @param requestChannel JAVADOC.
     * @param eventHandler JAVADOC.
     */
    public ActiveOperative(MessageChannel requestChannel) {
        this.requestChannel = requestChannel;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param request JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message<?> process(final Message<?> request) {
        requestChannel.send(request);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Sent " + request + " on channel " + requestChannel);
        }

        MessageHeaders requestHeaders = request.getHeaders();

        return loopProcess(requestHeaders);
    }

    /**
     * Default toString implementation
     *
     * @return This object as String.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
