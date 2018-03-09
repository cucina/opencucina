package org.cucina.conversation;

import org.cucina.conversation.events.ConversationEvent;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface EventHandler<T extends ConversationEvent> {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 * @return JAVADOC.
	 */
	ConversationEvent handleEvent(T event);
}
