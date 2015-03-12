
package org.cucina.engine.definition;

import java.util.Collection;

import org.cucina.engine.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <code>Decision</code>s are states that exit through one of the configured
 * list of {@link Transition Transitions} based on the
 * {@link Check Conditions} assigned to those {@link Transition Transitions}.
 *
 * @author Rob Harrop
 */
public class Decision
    extends AbstractState {
    private static final Logger LOG = LoggerFactory.getLogger(Decision.class);

    /**
     * JAVADOC
     *
     * @return JAVADOC
     */
    public Transition getDefaultTransition() {
        return null;
    }

    /**
     * JAVADOC
     *
     * @param from JAVADOC
     * @param executionContext JAVADOC
     */
    public void enter(Transition from, ExecutionContext executionContext) {
        super.enter(from, executionContext);
        leave((Transition) null, executionContext);
    }

    /**
     * JAVADOC
     *
     * @param transition
     *            JAVADOC
     * @param executionContext
     *            JAVADOC
     */
    protected void leaveInternal(Transition transition, ExecutionContext executionContext) {
        if (transition != null) {
            throw new IllegalArgumentException(
                "Cannot specify a transition when leaving a Decision state.");
        }

        Collection<Transition> transitions = getEnabledTransitions(executionContext);

        for (Transition exitTransition : transitions) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Evaluating transition:" + exitTransition);
            }

            if (exitTransition.isEnabled(executionContext)) {
                exitTransition.occur(executionContext);
                fireLeaveActions(executionContext);

                return;
            }
        }

        throw new IllegalStateException("Cannot leave Decision state - no transitions are active.");
    }
}
