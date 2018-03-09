package org.cucina.engine.server.handlers;

import org.cucina.conversation.events.ConversationEvent;
import org.cucina.engine.server.event.ProcessEvent;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ProcessEventHandler<T extends ProcessEvent> {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 * @return JAVADOC.
	 */
	ConversationEvent act(T event);
}
