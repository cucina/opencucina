package org.cucina.engine.listeners;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.Station;
import org.cucina.engine.definition.Token;
import org.cucina.engine.definition.Transition;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class DebuggingWorkflowListenerTest {
    private DebuggingWorkflowListener listener;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        listener = new DebuggingWorkflowListener();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEnteredState() {
        Station state = new Station();
        Transition from = new Transition();
        ExecutionContext executionContext = new DefaultExecutionContext(mock(Token.class), null,
                null, null);

        listener.enteredState(state, from, executionContext);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLeavingState() {
        Station state = new Station();
        Transition transition = new Transition();
        ExecutionContext executionContext = new DefaultExecutionContext(mock(Token.class), null,
                null, null);

        listener.leavingState(state, transition, executionContext);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testStartingSession() {
        listener.startingSession(new ProcessDefinition());
    }
}
