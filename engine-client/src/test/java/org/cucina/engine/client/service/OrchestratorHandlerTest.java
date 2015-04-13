package org.cucina.engine.client.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import org.cucina.engine.server.communication.ConversationContext;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
  */
public class OrchestratorHandlerTest {
    @Mock
    private Message<Object> message;
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
        when(operativeFactory.createOperative(anyString())).thenReturn(operative);
        when(operative.process(any(Message.class))).thenReturn(reply);
    }

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testHandleMessage() {
        when(message.getPayload()).thenReturn(new Object());
        handler.handleMessage(message);
        verify(replyChannel).send(reply);

        ArgumentCaptor<String> acs = ArgumentCaptor.forClass(String.class);

        verify(operativeFactory).createOperative(acs.capture());

        String cval = acs.getValue();

        verify(operativeFactory).releaseConversation(cval);

        ArgumentCaptor<Message> acm = ArgumentCaptor.forClass(Message.class);

        verify(operative).process(acm.capture());

        Message req = acm.getValue();

        assertEquals(cval, req.getHeaders().get(ConversationContext.CONVERSATION_ID));
    }
}
