package org.cucina.engine.client.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;

import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.engine.server.event.EngineEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This allows to provide execution on the same thread for callbacks.
 *
 * @author vlevine
 */
public abstract class AbstractOperative
    implements Operative {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractOperative.class);
    private EventHandler<EngineEvent> eventHandler;
    private MessageChannel callbackReplyChannel;
    private PollableChannel callbackChannel;
    private int timeout = 5000;

    /**
     *
     *
     * @param callbackChannel .
     */
    public void setCallbackChannel(PollableChannel callbackChannel) {
        this.callbackChannel = callbackChannel;
    }

    /**
     *
     *
     * @return .
     */
    public PollableChannel getCallbackChannel() {
        return callbackChannel;
    }

    /**
     * Fallback channel, if no <code>MessageHeaders.REPLY_CHANNEL</code> were
     * provided in the headers of callback message.
     *
     * @param callbackReplyChannel .
     */
    @Required
    public void setCallbackReplyChannel(MessageChannel callbackReplyChannel) {
        this.callbackReplyChannel = callbackReplyChannel;
    }

    /**
     *
     *
     * @param eventHandler .
     */
    @Required
    public void setEventHandler(EventHandler<EngineEvent> eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * Continues execution until either a callback payload is not a
     * CallbackEvent or there is a timeout receiving callback.
     *
     * @param headers .
     *
     * @return .
     */
    protected Message<?> loopProcess(Map<String, ?> headers) {
        while (true) {
            Message<?> callback = callbackChannel.receive(timeout);

            if (callback == null) {
                LOG.warn("No reply, possibly timeout");
                throw new RuntimeException("No reply, possibly timeout");
            }

            Object payload = callback.getPayload();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Payload=" + payload);
            }

            if (payload instanceof CallbackEvent) {
                EngineEvent callresult = eventHandler.handleEvent((EngineEvent) payload);
                Message<?> callmess = MessageBuilder.withPayload(callresult).copyHeaders(headers)
                                                    .setHeaderIfAbsent(ConversationContext.CONVERSATION_ID,
                        callback.getHeaders().get(ConversationContext.CONVERSATION_ID)).build();

                MessageChannel tempReplyChannel = (MessageChannel) callback.getHeaders()
                                                                           .get(MessageHeaders.REPLY_CHANNEL);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sending callback reply " + callmess + " on " +
                        ((tempReplyChannel == null) ? callbackReplyChannel : tempReplyChannel));
                }

                if (tempReplyChannel != null) {
                    tempReplyChannel.send(callmess);
                } else {
                    callbackReplyChannel.send(callmess);
                }

                continue;
            }

            return callback;
        }
    }
}
