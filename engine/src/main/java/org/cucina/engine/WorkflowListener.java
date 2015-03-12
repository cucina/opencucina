
package org.cucina.engine;

import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface WorkflowListener {
    /**
     * JAVADOC.
     *
     * @param state JAVADOC.
     * @param from JAVADOC.
     * @param executionContext JAVADOC.
     */
    void enteredState(State state, Transition from, ExecutionContext executionContext);

    /**
     * JAVADOC.
     *
     * @param state JAVADOC.
     * @param transition JAVADOC.
     * @param executionContext JAVADOC.
     */
    void leavingState(State state, Transition transition, ExecutionContext executionContext);

    /**
     * JAVADOC.
     *
     * @param workflowDefinition JAVADOC.
     */
    void startingSession(ProcessDefinition workflowDefinition);
}
