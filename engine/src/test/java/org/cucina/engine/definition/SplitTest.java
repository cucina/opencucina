package org.cucina.engine.definition;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.TokenFactory;
import org.cucina.engine.testassist.Foo;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyObject;

import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SplitTest {
    @Mock
    private ExecutionContext executionContext;
    @Mock
    private TokenFactory tokenFactory;

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
        Split split = new Split();
        Transition from = mock(Transition.class);
        Token token = mock(Token.class);

        when(executionContext.getToken()).thenReturn(token);
        when(executionContext.getProcessDriver()).thenReturn(mock(ProcessDriver.class));

        split.enter(from, executionContext);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetDefaultTransition() {
        Split split = new Split();

        assertNull("Default transition must be null", split.getDefaultTransition());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLeaveInternal() {
        Split split = new Split();

        split.setId("split");

        ProcessDefinition definition = mock(ProcessDefinition.class);

        split.setProcessDefinition(definition);

        Transition t1 = mock(Transition.class);

        when(t1.isDefault()).thenReturn(false);
        when(t1.getId()).thenReturn("t1");
        t1.setProcessDefinition(definition);
        t1.setInput(split);
        when(t1.getInput()).thenReturn(split);
        t1.checkConditions((ExecutionContext) anyObject());
        t1.occur((ExecutionContext) anyObject());
        split.addTransition(t1);

        Transition t2 = mock(Transition.class);

        when(t2.isDefault()).thenReturn(false);
        when(t2.getId()).thenReturn("t2");
        t2.setProcessDefinition(definition);
        t2.setInput(split);
        when(t2.getInput()).thenReturn(split);
        t2.checkConditions((ExecutionContext) anyObject());
        t2.occur((ExecutionContext) anyObject());
        split.addTransition(t2);

        Token token = mock(Token.class);

        when(token.getPlaceId()).thenReturn("split");
        when(executionContext.getToken()).thenReturn(token);

        Foo foo = new Foo();

        doReturn(foo).when(token).getDomainObject();
        when(executionContext.getTokenFactory()).thenReturn(tokenFactory);

        Token tok1 = mock(Token.class);

        when(tokenFactory.createToken(definition, foo)).thenReturn(tok1);

        Token tok2 = mock(Token.class);

        when(tokenFactory.createToken(definition, foo)).thenReturn(tok2);
        token.addChild(tok1);
        token.addChild(tok2);
        token.setPlaceId(null);
        split.leaveInternal(mock(Transition.class), executionContext);
    }
}
