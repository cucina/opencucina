package org.cucina.engine.definition;

import java.util.Collections;
import java.util.List;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.ProcessDriverFactory;
import org.cucina.engine.WorkflowListener;
import org.cucina.engine.checks.AbstractCheck;
import org.cucina.engine.testadapters.MockWorkflowDefinitionBuilder;
import org.cucina.engine.testadapters.StandardOutputWorkflowListener;
import org.cucina.engine.testassist.Foo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * @author Rob Harrop
 * @author vlevine
 */
public class StateModelTest {
    @Captor
    private ArgumentCaptor<List<Operation>> captor;
    @Mock
    private ProcessDriver executor;
    @Mock
    private ProcessDriverFactory executorFactory;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executorFactory.getExecutor()).thenReturn(executor);
    }

    /**
     * JAVADOC
     */
    @Test
    public void testActionExecution() {
        Operation enterAction = mock(Operation.class);
        Operation leaveAction = mock(Operation.class);

        ProcessDefinition definition = new ProcessDefinition();

        StartStation start = new StartStation();

        start.setId("start");
        definition.setStartState(start);

        start.addEnterAction(enterAction);
        start.addLeaveAction(leaveAction);

        Transition transition = new Transition();

        start.addTransition(transition);

        EndStation end = new EndStation();

        end.setId("end");
        transition.setOutput(end);

        Token token = mock(Token.class);
        DefaultExecutionContext executionContext = new DefaultExecutionContext(token, null, null,
                executorFactory);

        start.enter(null, executionContext);
        verify(token).setPlaceId("start");
        when(token.getPlaceId()).thenReturn("start");
        start.leave(transition, executionContext);
        verify(executor, times(4)).execute(captor.capture(), eq(executionContext));

        List<List<Operation>> values = captor.getAllValues();

        assertTrue(values.get(0).contains(enterAction));
        assertTrue(values.get(1).contains(leaveAction));
        assertTrue(values.get(2).isEmpty());
        assertTrue(values.get(3).isEmpty());
    }

    /**
     * JAVADOC
     */
    @Test
    public void testAddOutputPlaceToTransition() {
        ProcessDefinition workflowDefinition = new ProcessDefinition();

        Station state = new Station();

        state.setWorkflowDefinition(workflowDefinition);

        Transition transition = new Transition();

        state.addTransition(transition);
        transition.setOutput(state);

        assertEquals(workflowDefinition, state.getWorkflowDefinition());
    }

    /**
     * JAVADOC
     */
    @Test
    public void testAddTransitionToInputPlace() {
        ProcessDefinition workflowDefinition = new ProcessDefinition();
        Station state = new Station();

        state.setWorkflowDefinition(workflowDefinition);

        Transition transition = new Transition();

        state.addTransition(transition);

        assertEquals(workflowDefinition, transition.getWorkflowDefinition());
        assertEquals(state, transition.getInput());
    }

    /**
     * JAVADOC
     */
    @Test
    public void testDecisionExecution() {
        ProcessDefinition definition = new ProcessDefinition();

        definition.setId("decision");

        // create the decision
        Decision decision = new Decision();

        decision.setId("decision");
        decision.setWorkflowDefinition(definition);

        // create the Coke state
        Station choice1 = new Station();

        decision.setId("choice1");

        // create the Beer state
        Station choice2 = new Station();

        decision.setId("choice2");

        // create the transition to choice1
        Transition toChoice1 = new Transition();

        toChoice1.setId("toChoice1");
        decision.addTransition(toChoice1);
        toChoice1.addCondition(new AbstractCheck() {
                public boolean test(ExecutionContext executionContext) {
                    Foo p = (Foo) executionContext.getToken().getDomainObject();

                    return (p.getValue() < 18);
                }
            });
        toChoice1.setOutput(choice1);

        // create the transition to choice2
        Transition toChoice2 = new Transition();

        toChoice2.setId("toChoice2");
        decision.addTransition(toChoice2);
        toChoice2.setOutput(choice2);
        toChoice2.addCondition(new AbstractCheck() {
                public boolean test(ExecutionContext executionContext) {
                    Foo p = (Foo) executionContext.getToken().getDomainObject();

                    return (p.getValue() >= 18);
                }
            });

        Token token = mock(Token.class);

        Foo p = new Foo();

        p.setValue(17);
        token.setDomainObject(p);

        ExecutionContext executionContext = new DefaultExecutionContext(token, null,
                Collections.singletonList((WorkflowListener) new StandardOutputWorkflowListener()),
                executorFactory);

        when(executor.test(any(Check.class), eq(executionContext))).thenReturn(true);
        when(token.getPlaceId()).thenReturn("choice2");
        decision.enter(null, executionContext);
        verify(token, times(2)).setPlaceId(null);

        // now try transition for people that over 18
        p = new Foo();
        p.setValue(29);
        executionContext.getToken().setDomainObject(p);
        when(token.getPlaceId()).thenReturn("choice2");
        decision.enter(null, executionContext);
        verify(token, times(4)).setPlaceId(null);
    }

    /**
     * JAVADOC
     */
    @Test
    public void testFindTransitionByName() {
        ProcessDefinition definition = MockWorkflowDefinitionBuilder.buildHelloWorldDefinition();

        State startState = definition.getStartState();

        Transition transition = startState.getTransition("toHelloWorld");

        assertNotNull(transition);

        try {
            startState.getTransition("fooTransition");
            fail("Accessing a non-existent transition should throw TransitionNotFoundException");
        } catch (TransitionNotFoundException ex) {
            // desired
        }
    }

    /**
     * JAVADOC
     */
    @Test
    public void testGetDefaultState() {
        // todo: test for invalid get default state
        State state = MockWorkflowDefinitionBuilder.buildHelloWorldDefinition().getStartState();
        Transition transition = state.getDefaultTransition();

        assertNotNull(transition);
    }

    /**
     * JAVADOC
     */
    @Test
    public void testHelloWorldWorkflow() {
        ProcessDefinition definition = MockWorkflowDefinitionBuilder.buildHelloWorldDefinition();

        assertNotNull(definition.getStartState());
        assertEquals("start", definition.getStartState().getId());

        Token token = mock(Token.class);

        when(token.getPlaceId()).thenReturn(definition.getStartState().getId());

        DefaultExecutionContext executionContext = new DefaultExecutionContext(token, null, null,
                executorFactory);

        executionContext.setToken(token);

        definition.getStartState().enter(null, executionContext);
        verify(token).setPlaceId(definition.getStartState().getId());

        // leave via a transition now
        definition.findPlace(token.getPlaceId()).getDefaultTransition().occur(executionContext);
        verify(token).setPlaceId("helloWorld");
        when(token.getPlaceId()).thenReturn("helloWorld");

        // should now be on the hello world state
        assertEquals(Station.class, definition.findPlace(token.getPlaceId()).getClass());

        // check that the transition from start to hello is not active
        assertFalse(definition.getStartState().getDefaultTransition().isEnabled(executionContext));

        // fire transition to end
        definition.findPlace(token.getPlaceId()).getDefaultTransition().occur(executionContext);
        verify(token).setPlaceId("end");
        when(token.getPlaceId()).thenReturn("end");
        assertEquals(EndStation.class, definition.findPlace(token.getPlaceId()).getClass());
    }
}