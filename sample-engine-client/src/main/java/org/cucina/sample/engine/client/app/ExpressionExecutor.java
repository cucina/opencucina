
package org.cucina.sample.engine.client.app;


/**
 * Provides expression execution functionality.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ExpressionExecutor {
    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param context JAVADOC.
     * @param expression JAVADOC.
     *
     * @return JAVADOC.
     */
    <T> T evaluate(Object context, String expression);

    /**
     * Set value using expression on context.
     *
     * @param context Object.
     * @param expression String.
     * @param value Object.
     */
    void set(Object context, String expression, Object value);
}
