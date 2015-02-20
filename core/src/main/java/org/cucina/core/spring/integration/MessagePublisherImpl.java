
package org.cucina.core.spring.integration;

import java.util.Map;

import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.Assert;


/**
 * Creates a new MessagePublisher which delegates to spring integration channel.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MessagePublisherImpl
    implements MessagePublisher {
    private MessageChannel channel;
    private MessagingTemplate template;

    /**
     * Creates a new MessagePublisherImpl object.
     *
     * @param channel MessageChannel.
     */
    public MessagePublisherImpl(MessageChannel channel) {
        super();
        Assert.notNull(channel, "channel cannot be null");
        this.channel = channel;
        this.template = new MessagingTemplate(channel);
    }

    /**
     * Convenience method which delegates to publish method with headers.
     *
     * @param payload Object.
     */
    @Override
    public void publish(Object payload) {
        publish(payload, null);
    }

    /**
    * Publish payload with headers
    *
    * @param payload Object.
    * @param headers Map<String, Object>.
    */
    @Override
    public void publish(Object payload, Map<String, Object> headers) {
        Assert.notNull(payload, "Requires non null payload");
        template.convertAndSend(channel, payload, headers);
    }
}
