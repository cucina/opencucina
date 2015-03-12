
package org.cucina.engine.checks;

import org.cucina.engine.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * Expression condition using Spring EL against the executionContext.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ExpressionCheck
    extends AbstractCheck {
    private static final Logger LOG = LoggerFactory.getLogger(ExpressionCheck.class);
    private String expression;

    /**
     * Set expression
     *
     * @param expression String.
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
    * Tests the condition
    *
    * @param executionContext ExecutionContext.
    *
    * @return boolean true or false depending upon outcome of expression.
    */
    @Override
    public boolean test(ExecutionContext executionContext) {
        Assert.notNull(executionContext, "executionContext cannot be null");
        Assert.notNull(expression, "expression cannot be null");

        Object result = executionContext.getExpressionExecutor()
                                        .evaluate(executionContext, expression);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Result returned for expression [" + expression + "] is [" + result + "]");
        }

        return (result instanceof Boolean) ? ((Boolean) result).booleanValue() : false;
    }
}
