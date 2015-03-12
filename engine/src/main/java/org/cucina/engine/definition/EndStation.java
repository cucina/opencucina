package org.cucina.engine.definition;

import org.cucina.engine.ExecutionContext;


/**
 * The {@link State} that signifies the end of a particular
 * {@link ProcessDefinition}.
 *
 * @author Rob Harrop
 */
public class EndStation
    extends AbstractState {
    /**
     * Throws an {@link IllegalStateException} since you cannot leave the
     * terminal state of a workflow instance.
     */
    protected void leaveInternal(Transition transition, ExecutionContext executionContext) {
        throw new IllegalStateException("Cannot leave from the EndState");
    }
}
