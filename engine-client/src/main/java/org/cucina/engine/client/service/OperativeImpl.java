package org.cucina.engine.client.service;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;

import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.engine.server.event.CommitEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.RollbackEvent;
import org.cucina.engine.server.event.workflow.ValueEvent;


/**
 * this allows to provide execution on the same thread
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class OperativeImpl
    implements Operative {
    private static final Logger LOG = LoggerFactory.getLogger(OperativeImpl.class);
    private EventHandler<EngineEvent> eventHandler;

    // to send response to a callback request
    private MessageChannel callbackReplyChannel;

    // to forward request to the server
    private MessageChannel requestChannel;

    // to get callbacks on this
    private PollableChannel replyChannel;
    private int timeout = 5000; // TODO make it configurable and self-adjusting

    /**
     * Creates a new OperativeImpl object.
     *
     * @param requestChannel JAVADOC.
     * @param eventHandler JAVADOC.
     */
    public OperativeImpl(MessageChannel requestChannel, MessageChannel callbackReplyChannel,
        EventHandler<EngineEvent> eventHandler) {
        this.requestChannel = requestChannel;
        this.eventHandler = eventHandler;
        this.callbackReplyChannel = callbackReplyChannel;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param replyChannel JAVADOC.
     */
    public void setReplyChannel(PollableChannel replyChannel) {
        this.replyChannel = replyChannel;
    }

    /**
     *
     *
     * @return .
     */
    @Override
    public MessageChannel getReplyChannel() {
        return replyChannel;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param request JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message<?> process(final Message<?> request) {
        requestChannel.send(request);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Sent " + request + " on channel " + requestChannel);
        }

        MessageHeaders requestHeaders = request.getHeaders();

        while (true) {
            Message<?> reply = replyChannel.receive(timeout);

            if (reply == null) {
                LOG.warn("No reply, possibly timeout");
                throw new RuntimeException("No reply, possibly timeout");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("conversationId '" +
                    reply.getHeaders().get(ConversationContext.CONVERSATION_ID) + "'");
            }

            Object payload = reply.getPayload();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Payload=" + payload);
            }

            if (payload instanceof CallbackEvent) {
                EngineEvent callresult = eventHandler.handleEvent((EngineEvent) payload);
                Message<?> callmess = MessageBuilder.withPayload(callresult)
                                                    .copyHeaders(requestHeaders).build();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sending callback reply " + callmess + " on " + callbackReplyChannel);
                }

                MessageChannel tempReplyChannel = (MessageChannel) reply.getHeaders()
                                                                        .get(MessageHeaders.REPLY_CHANNEL);

                if (tempReplyChannel != null) {
                    tempReplyChannel.send(callmess);
                } else {
                    callbackReplyChannel.send(callmess);
                }

                continue;
            }

            if ((payload instanceof CommitEvent) || (payload instanceof RollbackEvent)|| (payload instanceof ValueEvent) ) {
                return reply;
            }

            LOG.warn("Payload not handleable " + payload);
        }
    }

    /**
         * Default toString implementation
         *
         * @return This object as String.
         */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
