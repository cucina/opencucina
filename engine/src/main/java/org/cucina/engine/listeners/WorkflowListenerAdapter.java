package org.cucina.engine.listeners;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.WorkflowListener;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;


/**
 * Noop class for convenience.
 *
 * @author vlevine
 * @version $Revision: 1.1 $
 */
public abstract class WorkflowListenerAdapter
		implements WorkflowListener {
	/**
	 * JAVADOC.
	 *
	 * @param state            JAVADOC.
	 * @param from             JAVADOC.
	 * @param executionContext JAVADOC.
	 */
	public void enteredState(State state, Transition from, ExecutionContext executionContext) {
	}

	/**
	 * JAVADOC.
	 *
	 * @param state            JAVADOC.
	 * @param transition       JAVADOC.
	 * @param executionContext JAVADOC.
	 */
	public void leavingState(State state, Transition transition, ExecutionContext executionContext) {
	}

	/**
	 * JAVADOC.
	 *
	 * @param workflowDefinition JAVADOC.
	 */
	public void startingSession(ProcessDefinition workflowDefinition) {
	}
}
