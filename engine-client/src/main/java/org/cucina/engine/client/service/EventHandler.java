
package org.cucina.engine.client.service;

import org.cucina.engine.server.event.EngineEvent;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface EventHandler<T extends EngineEvent> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     *
     * @return JAVADOC.
     */
    EngineEvent handleEvent(T event);
}
