
package org.cucina.engine.definition;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.cucina.core.spring.ExpressionExecutor;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.ProcessDriver;
import org.cucina.engine.TokenFactory;
import org.cucina.engine.testassist.Bar;
import org.cucina.engine.testassist.Foo;
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
public class CollectionSplitTest {
    @Mock
    private ExecutionContext executionContext;
    @Mock
    private ExpressionExecutor expressionExecutor;
    @Mock
    private TokenFactory tokenFactory;
    @Mock
    private ProcessDefinition processDefinition;
    @Mock
    private ProcessDriver executor;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(executionContext.getProcessDriver()).thenReturn(executor);
        when(executionContext.getExpressionExecutor()).thenReturn(expressionExecutor);
        when(executionContext.getTokenFactory()).thenReturn(tokenFactory);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testEnter()
        throws Exception {
        CollectionSplit split = new CollectionSplit();

        split.setId("csplit");

        split.setProcessDefinition(processDefinition);

        Transition from = mock(Transition.class);
        Token token = mock(Token.class);

        Foo foo = new Foo();
        Collection<Bar> bars = new ArrayList<Bar>();
        Bar b1 = new Bar();

        bars.add(b1);

        Bar b2 = new Bar();

        bars.add(b2);
        foo.setBars(bars);

        //        when(token.getDomainObject()).thenReturn(foo);
        token.setPlaceId("csplit");

        when(executionContext.getToken()).thenReturn(token);
        when(expressionExecutor.evaluate(executionContext, "token.domainObject.bars"))
            .thenReturn(bars);
        split.setCollectionExpression("token.domainObject.bars");

        Token tok1 = mock(Token.class);

        when(tokenFactory.createToken(processDefinition, b1)).thenReturn(tok1);

        Token tok2 = mock(Token.class);

        when(tokenFactory.createToken(processDefinition, b2)).thenReturn(tok2);

        Transition transition = mock(Transition.class);

        when(transition.getId()).thenReturn("t1");
        when(transition.isDefault()).thenReturn(true);
        transition.setProcessDefinition(processDefinition);
        transition.setInput(split);

        Transition traClone1 = mock(Transition.class);

        traClone1.checkConditions((ExecutionContext) anyObject());
        traClone1.occur((ExecutionContext) anyObject());
        when(transition.clone()).thenReturn(traClone1);

        Transition traClone2 = mock(Transition.class);

        traClone2.checkConditions((ExecutionContext) anyObject());
        traClone2.occur((ExecutionContext) anyObject());
        when(transition.clone()).thenReturn(traClone2);

        token.addChild(tok1);
        token.addChild(tok2);
        split.addTransition(transition);
        split.enter(from, executionContext);
    }
}
