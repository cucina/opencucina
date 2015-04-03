package org.cucina.engine.client.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


/**
 * 
 *
 * @author vlevine
  */
public interface Operative {
    /**
     *
     *
     * @return .
     */
    MessageChannel getReplyChannel();

    /**
     *
     *
     * @param request .
     *
     * @return .
     */
    Message<?> process(Message<?> request);
}
