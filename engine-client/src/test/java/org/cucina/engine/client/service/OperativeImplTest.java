package org.cucina.engine.client.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.PollableChannel;

import org.cucina.engine.server.communication.ConversationContext;
import org.cucina.engine.server.event.CallbackEvent;
import org.cucina.engine.server.event.CommitEvent;
import org.cucina.engine.server.event.EngineEvent;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
  */
public class OperativeImplTest {
    @Mock
    private EventHandler<EngineEvent> eventHandler;
    @Mock
    private Message<?> request;
    @Mock
    private MessageChannel callbackReplyChannel;
    @Mock
    private MessageChannel requestChannel;
    private OperativeImpl operative;
    @Mock
    private PollableChannel replyChannel;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        operative = new OperativeImpl(requestChannel, callbackReplyChannel, eventHandler);
        operative.setReplyChannel(replyChannel);
    }

    /**
     *
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testProcess() {
        Map<String, Object> hs = new HashMap<String, Object>();

        hs.put(ConversationContext.CONVERSATION_ID, "calue");

        MessageHeaders headers = new MessageHeaders(hs);

        when(request.getHeaders()).thenReturn(headers);

        Message reply = mock(Message.class);
        MessageHeaders replyHeaders = mock(MessageHeaders.class);

        when(reply.getHeaders()).thenReturn(replyHeaders);

        CallbackEvent ce = new CallbackEvent();
        CommitEvent coe = new CommitEvent();

        when(reply.getPayload()).thenReturn(ce).thenReturn(coe);
        when(replyChannel.receive(5000)).thenReturn(reply);

        EngineEvent ee = mock(EngineEvent.class);

        when(eventHandler.handleEvent(ce)).thenReturn(ee);
        operative.process(request);
        verify(requestChannel).send(request);

        ArgumentCaptor<Message> mac = ArgumentCaptor.forClass(Message.class);

        verify(callbackReplyChannel).send(mac.capture());

        Message callmess = mac.getValue();

        assertEquals(ee, callmess.getPayload());
        assertEquals(headers.get(ConversationContext.CONVERSATION_ID),
            callmess.getHeaders().get(ConversationContext.CONVERSATION_ID));
    }
}
