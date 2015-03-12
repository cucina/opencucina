package org.cucina.engine.operations;

import org.cucina.core.spring.ExpressionExecutor;

import org.cucina.engine.ExecutionContext;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ParameterSetterOperationTest {
    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testExecute() {
        String key = "key";
        String value = "T(java.lang.System).getProperty('name')";
        ParameterValueExtractorOperation action = new ParameterValueExtractorOperation();

        ExecutionContext executionContext = mock(ExecutionContext.class);
        ExpressionExecutor expressionExecutor = mock(ExpressionExecutor.class);

        when(executionContext.getExpressionExecutor()).thenReturn(expressionExecutor);
        when(expressionExecutor.evaluate(executionContext, value)).thenReturn(key);

        action.setParameterName(key);
        action.setValueExpression(value);
        action.execute(executionContext);
    }
}
