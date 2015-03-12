package org.cucina.engine.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.context.ApplicationContext;

import org.cucina.engine.event.PluralTransitionEvent;
import org.cucina.engine.listeners.HistoryListener;
import org.cucina.engine.testassist.Bar;
import org.cucina.engine.testassist.Foo;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Tests WorkflowTransitionExecutor functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class BulkTransitionsExecutorTest {
    /**
     * Execute transitions
     */
    @Test
    public void execute() {
        PluralTransitionEvent wrapper = new PluralTransitionEvent("id1", "workflow1", Foo.TYPE,
                null,
                Collections.<String, Object>singletonMap(HistoryListener.HISTORY_RECORD,
                    "Not on my watch"));
        PluralTransitionEvent wrapper2 = new PluralTransitionEvent("id2", "workflow2", Bar.TYPE,
                null, null);

        Collection<PluralTransitionEvent> transitions = new ArrayList<PluralTransitionEvent>();

        transitions.add(wrapper);
        transitions.add(wrapper2);

        ApplicationContext ac = mock(ApplicationContext.class);

        BulkTransitionsExecutor executor = new BulkTransitionsExecutor(transitions);

        executor.setApplicationContext(ac);

        executor.execute();

        verify(ac).publishEvent(wrapper);
        verify(ac).publishEvent(wrapper2);
    }

    /**
     * No transitions
     */
    @Test
    public void executeNoTransitions() {
        BulkTransitionsExecutor executor = new BulkTransitionsExecutor(null);

        executor.execute();
    }
}
