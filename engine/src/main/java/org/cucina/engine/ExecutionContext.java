package org.cucina.engine;

import java.util.List;
import java.util.Map;

import org.cucina.core.spring.ExpressionExecutor;

import org.cucina.engine.definition.Token;


/**
 * Simple interface used to encapsulate per-execution data to be passed to the
 * workflow engine. Can be accessed in
 * {@link org.cucina.engine.definition.Operation Actions} and
 * {@link org.cucina.engine.definition.Check Conditions} to perform
 * application-specific processing.
 *
 * @author Rob Harrop
 */
public interface ExecutionContext {
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    ExpressionExecutor getExpressionExecutor();

    /**
     * Gets the parameters for this execution.
     */
    Map<String, Object> getParameters();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    ProcessDriver getProcessDriver();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    ProcessDriverFactory getProcessDriverFactory();

    /**
     * Sets the {@link Token} for this particular execution.
     */
    void setToken(Token token);

    /**
     * Retrieves the {@link Token} used for this particular execution.
     */
    Token getToken();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    TokenFactory getTokenFactory();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    List<WorkflowListener> getWorkflowListeners();

    /**
     * JAVADOC.
     *
     * @param key
     *            JAVADOC.
     * @param value
     *            JAVADOC.
     */
    void addParameter(Object key, Object value);
}
