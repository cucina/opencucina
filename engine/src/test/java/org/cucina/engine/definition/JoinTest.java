package org.cucina.engine.definition;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.ProcessDriverFactory;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class JoinTest {
    @Mock
    private ExecutionContext executionContext;
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
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEnter() {
        Join join = new Join();

        join.setId("join");

        Transition transition = mock(Transition.class);

        when(transition.isDefault()).thenReturn(true);
        when(transition.getId()).thenReturn("tr1");
        transition.setWorkflowDefinition(null);
        transition.setInput(join);
        when(transition.getInput()).thenReturn(join);

        join.addTransition(transition);

        Token token = mock(Token.class);
        Token parent = mock(Token.class);

        when(parent.hasChildren()).thenReturn(false);
        when(parent.getParentToken()).thenReturn(null);
        token.setPlaceId("join");
        when(token.getParentToken()).thenReturn(parent);
        when(executionContext.getToken()).thenReturn(token);
        when(executionContext.getProcessDriver()).thenReturn(executor);
        when(executorFactory.getExecutor()).thenReturn(executor);
        when(executionContext.getProcessDriverFactory()).thenReturn(executorFactory);
        parent.removeChild(token);

        join.enter(null, executionContext);
        verify(transition).occur((ExecutionContext) anyObject());
    }
}
