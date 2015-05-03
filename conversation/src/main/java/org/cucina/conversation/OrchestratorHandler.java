package org.cucina.conversation;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.util.Assert;

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
    private MessageChannel conversationReply;
    private OperativeFactory operativeFactory;

    /**
     * Creates a new OrchestratorHandler object.
     *
     * @param conversationReply JAVADOC.
     * @param operativeFactory JAVADOC.
     */
    public OrchestratorHandler(MessageChannel conversationReply, OperativeFactory operativeFactory) {
        Assert.notNull(conversationReply, "conversationReply is null");
        this.conversationReply = conversationReply;
        Assert.notNull(operativeFactory, "operativeFactory is null");
        this.operativeFactory = operativeFactory;
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
        String conversationId = (String) message.getHeaders().get(Operative.CONVERSATION_ID);

        if (StringUtils.isEmpty(conversationId)) {
            conversationId = UUID.randomUUID().toString();
            LOG.debug("Created new conversation '" + conversationId + "'");
            message = MessageBuilder.fromMessage(message)
                                    .setHeader(Operative.CONVERSATION_ID, conversationId).build();
        } else {
            LOG.debug("Existing conversation '" + conversationId + "'");
        }

        Operative operative = operativeFactory.createOperative(conversationId);

        try {
            Message<?> reply = operative.process(message);

            if (LOG.isDebugEnabled()) {
                LOG.debug("reply=" + reply);
            }

            conversationReply.send(reply);
        } finally {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Releasing conversation '" + conversationId + "'");
            }

            operativeFactory.releaseConversation(conversationId);
        }
    }
}
