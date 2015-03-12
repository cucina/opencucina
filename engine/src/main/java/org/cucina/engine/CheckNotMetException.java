
package org.cucina.engine;


/**
 * @author Rob Harrop
 */
public class CheckNotMetException
    extends RuntimeException {
    /**
         *
         */
    private static final long serialVersionUID = -9704108275228696L;

    /**
     * Creates a new ConditionNotMetException object.
     *
     * @param message JAVADOC.
     */
    public CheckNotMetException(String message) {
        super(message);
    }
}
