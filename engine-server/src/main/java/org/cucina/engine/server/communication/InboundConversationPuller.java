package org.cucina.engine.server.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;


/**
 * 
 *
 * @author vlevine
  */
@Component
public class InboundConversationPuller {
	@Autowired
    private ConversationContext conversationContext;

    /**
     *
     *
     * @param message .
     *
     * @return .
     */
    @Transformer
    public Message<?> pullConversationId(Message<?> message) {
        String conversationId = (String) message.getHeaders()
                                                .get(ConversationContext.CONVERSATION_ID);

        conversationContext.setConversationId(conversationId);

        return message;
    }
}
