package org.cucina.engine;


/**
 * Thrown in response to a {@link ProcessSession#signal(ExecutionContext)
 * signal} failure.
 *
 * @author Rob Harrop
 * @author Rick Evans
 */
public class SignalFailedException
    extends RuntimeException {
    private static final long serialVersionUID = -5565176752762455838L;

    /**
     * Creates a new instance of the {@link SignalFailedException} class.
     *
     * @param message
     *            a detailed failure message about why the exception was thrown
     */
    public SignalFailedException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of the {@link SignalFailedException} class.
     *
     * @param message
     *            a detailed failure message about why the exception was thrown
     * @param cause
     *            the root cause of the exception
     */
    public SignalFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
