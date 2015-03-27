package org.cucina.engine.server.communication;

import org.apache.commons.lang3.StringUtils;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ConversationIdPuller
    extends ChannelInterceptorAdapter {
    private ConversationContext conversationContext;

    /**
     * Creates a new ConversationIdPuller object.
     *
     * @param conversationContext JAVADOC.
     */
    public ConversationIdPuller(ConversationContext conversationContext) {
        Assert.notNull(conversationContext, "conversationContext is null");
        this.conversationContext = conversationContext;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     * @param channel JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        String conversationId = (String) message.getHeaders()
                                                .get(ConversationContext.CONVERSATION_ID);

        if (StringUtils.isNoneEmpty(conversationId)) {
            conversationContext.setConversationId(conversationId);
        }

        return super.preSend(message, channel);
    }
}
