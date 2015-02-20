
package org.cucina.loader.processor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ProcessorEventListenerTest {
    private Object subject = new Object();
    private Processor processor;
    private ProcessorEventListener listener;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        processor = mock(Processor.class);

        Collection<Long> coll = new ArrayList<Long>();

        when(processor.process(subject)).thenReturn(coll);
        listener = new ProcessorEventListener(processor);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetOrder() {
        assertEquals(0, listener.getOrder());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testOnApplicationEvent() {
        listener.onApplicationEvent(new ProcessorEvent(subject));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSupportsEventType() {
        assertEquals(true, listener.supportsEventType(ProcessorEvent.class));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSupportsSourceType() {
        assertEquals(true, listener.supportsSourceType(Object.class));
    }
}
