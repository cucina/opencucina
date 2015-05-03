package org.cucina.engine.server.communication;

/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ConversationContext {
	//String CONVERSATION_ID = "conversationId";

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param conversationId JAVADOC.
	 */
	void setConversationId(String conversationId);

	/**
	 * Return the conversationId if there is one. If startNew is true, start a
	 * new conversation.
	 *
	 * @return conversationId.
	 */
	String getConversationId(boolean startNew);
}
