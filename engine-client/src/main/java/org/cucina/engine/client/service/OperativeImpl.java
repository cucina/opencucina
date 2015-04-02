package org.cucina.engine.client.service;

import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.engine.server.event.CommitEvent;
import org.cucina.engine.server.event.EngineEvent;
import org.cucina.engine.server.event.RollbackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;


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
            LOG.debug("Sent " + request + " on channel " + replyChannel);
        }

        while (true) {
            Message<?> reply = replyChannel.receive(5000);

            if (reply == null) {
                LOG.warn("No reply, possibly timeout");
                throw new RuntimeException("No reply, possibly timeout");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("conversationId '" +
                    reply.getHeaders().get(ConversationContext.CONVERSATION_ID) + "'");
            }

            Object payload = reply.getPayload();

            if (payload instanceof CallbackEvent) {
                EngineEvent callresult = eventHandler.handleEvent((EngineEvent) payload);
                Message<?> callmess = MessageBuilder.withPayload(callresult)
                                                    .copyHeaders(request.getHeaders()).build();

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

            if ((payload instanceof CommitEvent) || (payload instanceof RollbackEvent)) {
                return reply;
            }

            LOG.warn("Payload not handleable " + payload);
        }
    }
}
