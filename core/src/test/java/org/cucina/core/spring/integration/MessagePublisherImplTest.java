
package org.cucina.core.spring.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


/**
 * Tests that MessagePublisherImpl functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MessagePublisherImplTest {
    private static final Object PAYLOAD = new Object();
    private static final String KEY = "key";
    private static final Object VALUE = "value";
    private static final Map<String, Object> HEADERS = Collections.singletonMap(KEY, VALUE);
    @Mock
    private MessageChannel channel;
    private MessagePublisherImpl publisher;


    /**
     * Publishes payload.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void publishes() {
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        when(channel.send(captor.capture())).thenReturn(Boolean.TRUE);

        publisher.publish(PAYLOAD);

        assertEquals("Should have payload", PAYLOAD, captor.getValue().getPayload());
    }

    /**
     * Publishes payload and headers.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void publisheWithHeaders() {
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

        when(channel.send(captor.capture())).thenReturn(Boolean.TRUE);

        publisher.publish(PAYLOAD, HEADERS);

        assertEquals("Should have payload", PAYLOAD, captor.getValue().getPayload());
        assertEquals("Should have header", VALUE, captor.getValue().getHeaders().get(KEY));
    }

    /**
     * Requires payload
     */
    @Test(expected = IllegalArgumentException.class)
    public void requiresPayload() {
        when(channel.send(any(Message.class))).thenReturn(Boolean.TRUE);
        publisher.publish(null);
    }

    /**
     * Requires payload
     */
    @Test(expected = IllegalArgumentException.class)
    public void requiresPayloadWithHeaders() {
        when(channel.send(any(Message.class))).thenReturn(Boolean.TRUE);
        publisher.publish(null, HEADERS);
    }

    /**
     * Sets up for test
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        publisher = new MessagePublisherImpl(channel);
    }
}
