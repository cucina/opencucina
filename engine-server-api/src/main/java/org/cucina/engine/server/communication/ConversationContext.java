package org.cucina.engine.server.communication;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ConversationContext {
    String CONVERSATION_ID = "conversationId";

    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     */
    void setConversationId(String conversationId);

    /**
     * Return the conversationId if there is one.
     *
     * @return conversationId.
     */
    String getConversationId();

    /**
     * Starts a new conversation.
     *
     * @return String conversationId.
     *
     * @throws RuntimeException if there is an existing conversation in progress.
     */
    String startConversation();
}
