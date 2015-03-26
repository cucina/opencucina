package org.cucina.engine;

import org.cucina.engine.definition.Check;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.Token;
import org.cucina.engine.testadapters.MockProcessDefinitionBuilder;
import org.cucina.engine.testassist.Foo;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * @author Rob Harrop
 */
public class DefaultProcessSessionTest {
    @Mock
    private ProcessDriver executor;
    @Mock
    private ProcessDriverFactory executorFactory;
    @Mock
    private Token tokenX;
    @Mock
    private TokenFactory tokenFactory;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(executorFactory.getTokenFactory()).thenReturn(tokenFactory);
        when(executorFactory.getExecutor()).thenReturn(executor);
        when(tokenFactory.createToken((ProcessDefinition) any(), any())).thenReturn(tokenX);
    }

    /**
     * JAVADOC
     */
    @Test
    public void testChooseASoftDrinkWorkflow() {
        ProcessDefinition definition = MockProcessDefinitionBuilder.buildChooseADrinkDefinition();
        DefaultProcessSession session = new DefaultProcessSession(definition, executorFactory, null);

        Foo domainObject = new Foo();

        domainObject.setValue(90);
        when(tokenX.getPlaceId()).thenReturn("start").thenReturn("start").thenReturn("decision")
            .thenReturn("decision").thenReturn("decision").thenReturn("coke");
        when(tokenX.getDomainObject()).thenReturn(domainObject);
        when(executor.test(any(Check.class), any(ExecutionContext.class))).thenReturn(true);

        Token token = session.startProcessInstance(domainObject, null, null);

        assertEquals("Incorrect state", "coke", token.getPlaceId());

        String transitionId = session.getAvailableTransitions(session.createExecutionContext(
                    token, null)).iterator().next().getId();

        assertEquals("toEndFromCoke", transitionId);

        session.signal(session.createExecutionContext(token, null), transitionId);
    }

    /**
     * JAVADOC
     */
    @Test
    public void testChooseAToddlerDrinkWorkflow() {
        ProcessDefinition definition = MockProcessDefinitionBuilder.buildChooseADrinkDefinition();
        ProcessSession session = new DefaultProcessSession(definition, executorFactory, null);

        Foo domainObject = new Foo();

        when(tokenX.getPlaceId()).thenReturn("start").thenReturn("start").thenReturn("decision")
            .thenReturn("decision").thenReturn("decision").thenReturn("coke");
        when(tokenX.getDomainObject()).thenReturn(domainObject);
        when(executor.test(any(Check.class), any(ExecutionContext.class))).thenReturn(true);

        Token token = session.startProcessInstance(domainObject, null, null);

        // check drink
        assertEquals("Incorrect state", "coke", token.getPlaceId());
    }

    /**
     * JAVADOC
     */
    @Test
    public void testHelloWorldWorkflow() {
        ProcessDefinition definition = MockProcessDefinitionBuilder.buildHelloWorldDefinition();
        ProcessSession session = new DefaultProcessSession(definition, executorFactory, null);

        when(tokenX.getPlaceId()).thenReturn("start");

        session.startProcessInstance(new Foo(), null, null);

        verify(tokenX).setPlaceId("start");
        verify(tokenX).setPlaceId("helloWorld");
    }
}
