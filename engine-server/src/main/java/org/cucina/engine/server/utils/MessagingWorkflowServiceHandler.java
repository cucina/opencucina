package org.cucina.engine.server.utils;

import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.GetValueEvent;
import org.cucina.engine.server.event.workflow.ValueEvent;
import org.cucina.engine.server.event.workflow.WorkflowEvent;
import org.springframework.messaging.Message;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface MessagingWorkflowServiceHandler {
    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     *
     * @return JAVADOC.
     */
    ValueEvent getValue(GetValueEvent event);

    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     *
     * @return JAVADOC.
     */
    EngineEvent act(Message<WorkflowEvent> message);
}
