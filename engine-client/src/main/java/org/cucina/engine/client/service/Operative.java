package org.cucina.engine.client.service;

import org.springframework.messaging.Message;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface Operative {
    /**
     * JAVADOC Method Level Comments
     *
     * @param request JAVADOC.
     *
     * @return JAVADOC.
     */
    Message<?> process(Message<?> request);
}
