package org.cucina.engine.client.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import org.cucina.engine.server.event.RegistrationEvent;


/**
 * 
 *
 * @author vlevine
  */
public class ApplicationRegistratorTest {
    private ApplicationRegistrator registrator;
    @Mock
    private MessageChannel asyncChannel;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        registrator = new ApplicationRegistrator("applicationName", "myQueue", asyncChannel);
    }

    /**
     *
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testOnApplicationEvent() {
        registrator.onApplicationEvent(new ContextRefreshedEvent(mock(ApplicationContext.class)));

        ArgumentCaptor<Message> messc = ArgumentCaptor.forClass(Message.class);

        verify(asyncChannel).send(messc.capture());

        Message<?> message = messc.getValue();
        RegistrationEvent event = (RegistrationEvent) message.getPayload();

        assertEquals("jms://myQueue", event.getDestinationName());
        assertEquals("applicationName", event.getApplicationName());
    }
}
