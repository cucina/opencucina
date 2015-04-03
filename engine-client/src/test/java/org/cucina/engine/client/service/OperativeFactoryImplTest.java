package org.cucina.engine.client.service;

import org.springframework.context.ApplicationContext;
import org.springframework.integration.channel.QueueChannel;
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
public class OperativeFactoryImplTest {
    @Mock
    private ApplicationContext applicationContext;
    private OperativeFactoryImpl factory;
    @Mock
    private OperativeImpl operative;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        factory = new OperativeFactoryImpl();
        factory.setApplicationContext(applicationContext);
        when(applicationContext.getBean("operative", OperativeImpl.class)).thenReturn(operative);
        factory.afterPropertiesSet();
    }

    /**
     *
     */
    @Test
    public void testCreateOperative() {
        assertEquals(operative, factory.createOperative("conversationId"));

        ArgumentCaptor<QueueChannel> qac = ArgumentCaptor.forClass(QueueChannel.class);

        verify(operative).setReplyChannel(qac.capture());

        QueueChannel qc = qac.getValue();

        when(operative.getReplyChannel()).thenReturn(qc);
        assertEquals(qc, factory.findChannel("conversationId"));
        factory.releaseConversation("conversationId");
        assertNull(factory.findChannel("conversationId"));
    }
    
    // TODO test parallel scenarios
}
