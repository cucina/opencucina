package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.GetValueEvent;
import org.cucina.engine.server.event.ValueEvent;


/**
 * @param <T> .
 * @author vlevine
 */
public interface GetValueHandler<T extends GetValueEvent> {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 * @return JAVADOC.
	 */
	ValueEvent getValue(T event);
}
