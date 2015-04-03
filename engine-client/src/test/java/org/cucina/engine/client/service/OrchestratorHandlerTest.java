package org.cucina.engine.client.service;

import java.util.Collections;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

import org.cucina.engine.server.communication.ConversationContext;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
  */
public class OrchestratorHandlerTest {
    private static final String CVAL = "cval";
    @Mock
    private Message<?> message;
    @SuppressWarnings("rawtypes")
    @Mock
    private Message reply;
    @Mock
    private MessageChannel replyChannel;
    @Mock
    private Operative operative;
    @Mock
    private OperativeFactory operativeFactory;
    private OrchestratorHandler handler;

    /**
     *
     *
     * @throws Exception .
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new OrchestratorHandler(replyChannel, operativeFactory);
        when(operativeFactory.createOperative(CVAL)).thenReturn(operative);
        when(operative.process(message)).thenReturn(reply);
    }

    /**
     *
     */
    @Test
    public void testHandleMessage() {
        MessageHeaders headers = new MessageHeaders(Collections.singletonMap(
                    ConversationContext.CONVERSATION_ID, (Object) CVAL));

        when(message.getHeaders()).thenReturn(headers);

        handler.handleMessage(message);
        verify(replyChannel).send(reply);
        verify(operativeFactory).releaseConversation(CVAL);
    }
}
