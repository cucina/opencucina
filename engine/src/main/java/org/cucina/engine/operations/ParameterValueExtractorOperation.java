package org.cucina.engine.operations;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.definition.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * The operation evaluates the valueExpression and sets the parameter in
 * execution context to this value. Usually, for the parameter to be used
 * elsewhere in the workflow.
 */
public class ParameterValueExtractorOperation
    extends AbstractOperation {
    private static final Logger LOG = LoggerFactory.getLogger(ParameterValueExtractorOperation.class);

    /** Name of the target property. */
    private String parameterName;

    /** Expression obtaining value of the property. */
    private String valueExpression;

    /**
     * Set property name.
     *
     * @param propertyName
     *            name of the target property.
     */
    public final void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * Set property value.
     *
     * @param <code>Object</code> propertyValue.
     */
    public final void setValueExpression(String valueExpression) {
        this.valueExpression = valueExpression;
    }

    /**
     * Called by the WorkFlow Engine.
     *
     * @see Operation#execute(ExecutionContext)
     */
    public void execute(final ExecutionContext executionContext) {
        Assert.notNull(parameterName, "Parameter Name cannot be null.");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Attempting to set parameter [" + parameterName +
                "], with valueExpression [" + valueExpression + "]");
        }

        Object value = executionContext.getExpressionExecutor()
                                       .evaluate(executionContext, valueExpression);

        if (LOG.isDebugEnabled()) {
            LOG.debug("value [" + value + "]");
        }

        executionContext.addParameter(parameterName, value);
    }
}
