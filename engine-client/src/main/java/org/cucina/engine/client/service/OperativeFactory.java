package org.cucina.engine.client.service;

import org.springframework.messaging.MessageChannel;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface OperativeFactory {
    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     *
     * @return JAVADOC.
     */
    Operative createOperative(String conversationId);

    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     *
     * @return JAVADOC.
     */
    MessageChannel findChannel(String conversationId);

    /**
     * JAVADOC Method Level Comments
     *
     * @param conversationId JAVADOC.
     */
    void releaseConversation(String conversationId);
}
