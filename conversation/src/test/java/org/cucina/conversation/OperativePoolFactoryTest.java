package org.cucina.conversation;

import org.apache.commons.pool2.PooledObject;

import org.springframework.context.ApplicationContext;
import org.springframework.messaging.PollableChannel;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 *
 *
 * @author vlevine
  */
public class OperativePoolFactoryTest {
    @Mock
    private ApplicationContext applicationContext;
    private OperativePoolFactory factory;

    /**
     *
     *
     * @throws Exception .
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        factory = new OperativePoolFactory("operativeBeanName");
        factory.setApplicationContext(applicationContext);
        factory.afterPropertiesSet();
    }

    /**
     *
     *
     * @throws Exception .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testActivateObjectPooledObjectOfOperative()
        throws Exception {
        PooledObject<Operative> p = mock(PooledObject.class);
        Operative op = mock(Operative.class);

        when(p.getObject()).thenReturn(op);
        factory.activateObject(p);

        ArgumentCaptor<PollableChannel> ac = ArgumentCaptor.forClass(PollableChannel.class);

        verify(op).setCallbackChannel(ac.capture());

        PollableChannel pc = ac.getValue();

        when(op.getCallbackChannel()).thenReturn(pc);
        factory.passivateObject(p);
    }

    /**
     *
     */
    @Test
    public void testCreate()
        throws Exception {
        Operative oper = mock(Operative.class);

        when(applicationContext.getBean("operativeBeanName", Operative.class)).thenReturn(oper);
        assertEquals(oper, factory.create());
    }

    /**
     *
     */
    @Test
    public void testWrapOperative() {
        Operative op = mock(Operative.class);

        PooledObject<Operative> po = factory.wrap(op);

        assertEquals(op, po.getObject());
    }
}
