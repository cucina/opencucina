
package org.cucina.engine.definition;

import org.cucina.engine.SignalFailedException;


/**
 * Exception thrown when attempting to fire a {@link Transition} that is not
 * {@link Transition#isEnabled enabled}.
 *
 * @author robh
 */
public class TransitionNotFoundException
    extends SignalFailedException {
    private static final long serialVersionUID = -2802618240730378221L;

    /**
     * Creates a new TransitionNotFoundException object.
     *
     * @param message JAVADOC.
     */
    public TransitionNotFoundException(String message) {
        super(message);
    }
}
