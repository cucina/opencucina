package org.cucina.engine.client.service;

import org.apache.commons.pool2.impl.DefaultPooledObject;

import org.springframework.context.ApplicationContext;
import org.springframework.messaging.PollableChannel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
  */
public class OperativeFactoryImplTest {
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private Operative operative;
    @Mock
    private Operative poperative;
    private OperativeFactoryImpl factory;
    @Mock
    private OperativePoolFactory activePoolFactory;
    @Mock
    private OperativePoolFactory passivePoolFactory;
    @Mock
    private PollableChannel callbackChannel;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        factory = new OperativeFactoryImpl(activePoolFactory, passivePoolFactory);
        when(operative.getCallbackChannel()).thenReturn(callbackChannel);
        when(activePoolFactory.makeObject())
            .thenReturn(new DefaultPooledObject<Operative>(operative));
        when(passivePoolFactory.makeObject())
            .thenReturn(new DefaultPooledObject<Operative>(poperative));
    }

    /**
     * @throws Exception
     *
     */
    @Test
    public void testCreateOperative()
        throws Exception {
        assertEquals(operative, factory.createOperative("conversationId"));
        assertEquals(callbackChannel, factory.findChannel("conversationId"));
        factory.releaseConversation("conversationId");
        assertNull(factory.findChannel("conversationId"));
    }

    // TODO test parallel scenarios
}
