package org.cucina.engine.testadapters;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.WorkflowListener;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;


/**
 * Testing purposes only.
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class StandardOutputWorkflowListener
		implements WorkflowListener {
	/**
	 * JAVADOC.
	 *
	 * @param state            JAVADOC.
	 * @param from             JAVADOC.
	 * @param executionContext JAVADOC.
	 */
	public void enteredState(State state, Transition from, ExecutionContext executionContext) {
		System.out.println("entered state " + state + " from transition:" + from + " using token:" +
				executionContext.getToken());
	}

	/**
	 * JAVADOC.
	 *
	 * @param state            JAVADOC.
	 * @param transition       JAVADOC.
	 * @param executionContext JAVADOC.
	 */
	public void leavingState(State state, Transition transition, ExecutionContext executionContext) {
		System.out.println("leaving state " + state + " using transition:" + transition +
				" using token:" + executionContext.getToken());
	}

	/**
	 * JAVADOC.
	 *
	 * @param workflowDefinition JAVADOC.
	 */
	public void startingSession(ProcessDefinition workflowDefinition) {
		System.out.println("starting session from definition:" + workflowDefinition);
	}
}
