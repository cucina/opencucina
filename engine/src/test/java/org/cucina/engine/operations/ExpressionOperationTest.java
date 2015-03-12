
package org.cucina.engine.operations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.core.spring.ExpressionExecutor;
import org.cucina.engine.ExecutionContext;
import org.junit.Before;
import org.junit.Test;


/**
 * Ensure {@link ExpressionOperation} functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ExpressionOperationTest {
    private static final String EXPRESSION_FROM = "@foo.name";
    private static final String EXPRESSION_TO = "token.domainObject.name";
    private static final String VALUE = "mikey";
    private ExpressionOperation action;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void before()
        throws Exception {
        action = new ExpressionOperation();
        action.setExpressionFrom(EXPRESSION_FROM);
        action.setExpressionTo(EXPRESSION_TO);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = IllegalArgumentException.class)
    public void contextRequired() {
        action.execute(null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void setsValue() {
        ExecutionContext executionContext = mock(ExecutionContext.class);
        ExpressionExecutor expressionExecutor = mock(ExpressionExecutor.class);

        when(executionContext.getExpressionExecutor()).thenReturn(expressionExecutor);
        when(expressionExecutor.evaluate(executionContext, EXPRESSION_FROM)).thenReturn(VALUE);

        action.execute(executionContext);
        verify(expressionExecutor).set(executionContext, EXPRESSION_TO, VALUE);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetValueOnly() {
        action.setExpressionTo(null);

        ExecutionContext executionContext = mock(ExecutionContext.class);
        ExpressionExecutor expressionExecutor = mock(ExpressionExecutor.class);

        when(expressionExecutor.evaluate(executionContext, EXPRESSION_FROM)).thenReturn(null);
        when(executionContext.getExpressionExecutor()).thenReturn(expressionExecutor);

        action.execute(executionContext);
    }
}
