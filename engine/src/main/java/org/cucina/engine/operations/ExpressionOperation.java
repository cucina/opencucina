package org.cucina.engine.operations;

import org.apache.commons.lang3.StringUtils;
import org.cucina.engine.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * Action that has getter and setter expressions. Makes use of the configured
 * {@link ExpressionExecutor}. Copies value obtained <code>expressionFrom</code>
 * applying it using <code>expressionTo</code>. Both of these expressions are
 * applied on the executionContext.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ExpressionOperation
    extends AbstractOperation {
    private static final Logger LOG = LoggerFactory.getLogger(ExpressionOperation.class);
    private String expressionFrom;
    private String expressionTo;

    /**
     * Set expressionFrom, the expression to get the property to set to
     * expressionTo.
     *
     * @param expressionFrom
     *            String.
     */
    public void setExpressionFrom(String expressionFrom) {
        this.expressionFrom = expressionFrom;
    }

    /**
     * Set expressionTo, the expression to set the property to returned from
     * expressionFrom evaluation.
     *
     * @param expressionTo
     *            String.
     */
    public void setExpressionTo(String expressionTo) {
        this.expressionTo = expressionTo;
    }

    /**
     * Uses {@link ExpressionExecutor} to get and set properties.
     *
     * @param executionContext
     *            ExecutionContext.
     */
    @Override
    public void execute(ExecutionContext executionContext) {
        Assert.notNull(executionContext, "executionContext cannot be null");

        if (LOG.isDebugEnabled()) {
            LOG.debug("expressionFrom [" + expressionFrom + "]");
        }

        Object value = executionContext.getExpressionExecutor()
                                       .evaluate(executionContext, expressionFrom);

        if (LOG.isDebugEnabled()) {
            LOG.debug("value [" + value + "]");
        }

        if (StringUtils.isNotEmpty(expressionTo)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("expressionFrom [" + expressionTo + "]");
            }

            executionContext.getExpressionExecutor().set(executionContext, expressionTo, value);
        }
    }
}
