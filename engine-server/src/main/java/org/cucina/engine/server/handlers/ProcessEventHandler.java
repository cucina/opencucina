package org.cucina.engine.server.handlers;

import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.workflow.ProcessEvent;


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
     *
     * @return JAVADOC.
     */
    EngineEvent act(T event);
}
