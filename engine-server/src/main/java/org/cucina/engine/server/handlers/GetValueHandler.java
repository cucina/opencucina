package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.GetValueEvent;
import org.cucina.engine.server.event.workflow.ValueEvent;


/**
 * 
 *
 * @author vlevine
  *
 * @param <T> .
 */
public interface GetValueHandler<T extends GetValueEvent> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     *
     * @return JAVADOC.
     */
    ValueEvent getValue(T event);
}
