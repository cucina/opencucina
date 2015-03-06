package org.cucina.loader.processor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cucina.loader.testassist.Foo;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ProcessorFactoryImplTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testProcessNoMatch() {
        ProcessorFactory factory = new ProcessorFactory();

        Map<Class<?>, Processor> processors = new HashMap<Class<?>, Processor>();

        factory.setProcessors(processors);
        factory.process(new Foo());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testProcessSuccess() {
        ProcessorFactory factory = new ProcessorFactory();

        Map<Class<?>, Processor> processors = new HashMap<Class<?>, Processor>();
        Processor processor = mock(Processor.class);
        Foo object = new Foo();

        when(processor.process(object)).thenReturn(Collections.singleton(1L));

        processors.put(Foo.class, processor);
        factory.setProcessors(processors);
        factory.process(object);
    }
}
