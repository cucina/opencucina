
package org.cucina.engine.listeners;

import org.cucina.engine.ExecutionContext;
import org.cucina.engine.WorkflowListener;
import org.cucina.engine.definition.ProcessDefinition;
import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class DebuggingWorkflowListener
    implements WorkflowListener {
    private static final Logger LOG = LoggerFactory.getLogger(DebuggingWorkflowListener.class);

    /**
     * JAVADOC.
     *
     * @param state JAVADOC.
     * @param from JAVADOC.
     * @param executionContext JAVADOC.
     */
    public void enteredState(State state, Transition from, ExecutionContext executionContext) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("entered state " + state + " from transition:" + from + " using token:" +
                executionContext.getToken());
        }
    }

    /**
     * JAVADOC.
     *
     * @param state JAVADOC.
     * @param transition JAVADOC.
     * @param executionContext JAVADOC.
     */
    public void leavingState(State state, Transition transition, ExecutionContext executionContext) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("leaving state " + state + " using transition:" + transition +
                " using token:" + executionContext.getToken());
        }
    }

    /**
     * JAVADOC.
     *
     * @param workflowDefinition JAVADOC.
     */
    public void startingSession(ProcessDefinition workflowDefinition) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("starting session from definition:" + workflowDefinition);
        }
    }
}
