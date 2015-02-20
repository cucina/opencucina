
package org.cucina.loader.agent;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.cucina.core.service.ContextService;
import org.cucina.loader.processor.Processor;
import org.cucina.loader.testassist.Foo;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ManagedPagingParallelAgentTest {
    @Mock
    private Processor processor;
    @Mock
    private ContextService contextService;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        LoggingEnabler.enableLog(ManagedPagingParallelAgent.class);
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testObtainNextPage() {
        final Collection<Foo> foos = new ArrayList<Foo>();

        foos.add(new Foo());

        ManagedPagingParallelAgent<Foo> executor = new ManagedPagingParallelAgent<Foo>(contextService,
                processor) {
                @Override
                protected Collection<Foo> obtainNextPage(int number) {
                    // TODO Auto-generated method stub
                    return foos;
                }
            };

        Collection<Foo> result = executor.obtainNextPage();

        assertEquals(foos, result);
    }
}
