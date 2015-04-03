package org.cucina.engine.client.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

import org.cucina.engine.server.communication.ConversationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
public class OrchestratorHandler
    implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(OrchestratorHandler.class);
    private MessageChannel replyChannel;
    private OperativeFactory operativeFactory;

    /**
     * Creates a new OrchestratorHandler object.
     *
     * @param replyChannel
     *            JAVADOC.
     * @param operativeFactory
     *            JAVADOC.
     */
    public OrchestratorHandler(MessageChannel replyChannel, OperativeFactory operativeFactory) {
        Assert.notNull(replyChannel, "replyChannel is null");
        this.replyChannel = replyChannel;
        Assert.notNull(operativeFactory, "operativeFactory is null");
        this.operativeFactory = operativeFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message
     *            JAVADOC.
     *
     * @throws MessagingException
     *             JAVADOC.
     */
    @Override
    public void handleMessage(Message<?> message)
        throws MessagingException {
        String conversationId = (String) message.getHeaders()
                                                .get(ConversationContext.CONVERSATION_ID);

        if (LOG.isDebugEnabled()) {
            LOG.debug("conversationId '" + conversationId + "'");
        }

        Assert.hasLength(conversationId, "conversationId is empty");

        Operative operative = operativeFactory.createOperative(conversationId);

        try {
            Message<?> reply = operative.process(message);

            replyChannel.send(reply);
        } finally {
            operativeFactory.releaseConversation(conversationId);
        }
    }
}
