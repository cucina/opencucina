package org.cucina.loader.processor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.springframework.context.ApplicationContext;


/**
 * Test that InputDataProcessorFactoryEventMulticaster functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ProcessorFactoryEventMulticasterTest {
    /**
     * Test publishes event
     */
    @Test
    public void publishEvent() {
        Object subject = new Object();

        ApplicationContext context = mock(ApplicationContext.class);

        ProcessorEventMulticaster multicaster = new ProcessorEventMulticaster();

        multicaster.setApplicationContext(context);
        multicaster.process(subject);

        verify(context).publishEvent(new ProcessorEvent(subject));
    }

    /**
     * Test that subject cannot be null
     */
    @Test(expected = IllegalArgumentException.class)
    public void subjectNotNull() {
        ProcessorEventMulticaster multicaster = new ProcessorEventMulticaster();

        multicaster.process(null);
    }
}
