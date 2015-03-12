package org.cucina.engine.definition;

import java.util.List;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.ProcessDriverFactory;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import org.mockito.InOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import org.mockito.Mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * Tests that State functions as expected
 *
 * @author thornton
 * @author vlevine
  */
public class StateTest {
    private static final String PLACE_ID = "myPlace";
    @Mock
    private ProcessDefinition workflowDefinition;
    @Mock
    private ProcessDriver executor;
    @Mock
    private ProcessDriverFactory executorFactory;
    private Station state;

    /**
     * Sets up for test
     *
     * @throws Exception.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        state = new Station();
        state.setId(PLACE_ID);
        when(workflowDefinition.getId()).thenReturn("wid");
        state.setWorkflowDefinition(workflowDefinition);
        when(executorFactory.getExecutor()).thenReturn(executor);
    }

    /**
     * Tests that only one default transition can be added
     */
    @Test
    public void testAddDefaultTransition() {
        Transition defaultTransition = new Transition();

        defaultTransition.setDefault(true);

        Transition defaultTransition2 = new Transition();

        defaultTransition2.setDefault(true);

        state.addTransition(defaultTransition);

        try {
            state.addTransition(defaultTransition2);
        } catch (IllegalArgumentException e) {
            return;
        }

        fail("Shouldn't allow duplicate default transitions");
    }

    /**
     * Checks that conditions are called prior to actions.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLeaveOrder() {
        Token token = mock(Token.class);

        when(token.getPlaceId()).thenReturn(PLACE_ID);

        ExecutionContext context = new DefaultExecutionContext(token, null, null, executorFactory);

        Operation action = mock(Operation.class);

        state.addLeaveAction(action);

        Transition transition = mock(Transition.class);

        state.leave(transition, context);

        InOrder inOrder = inOrder(executor, transition);

        inOrder.verify(transition).checkConditions(context);
        inOrder.verify(executor).execute(any(List.class), eq(context));
        inOrder.verify(transition).occur(context);
    }
}
