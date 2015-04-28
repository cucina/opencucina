package org.cucina.engine.client.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;


/**
 *
 *
 * @author vlevine
  */
public interface Operative {
    /**
     *
     *
     * @param channel .
     */
    void setCallbackChannel(PollableChannel channel);

    /**
     *
     *
     * @return .
     */
    PollableChannel getCallbackChannel();

    /**
     *
     *
     * @param request .
     *
     * @return .
     */
    Message<?> process(Message<?> request);
}
