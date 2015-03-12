package org.cucina.engine;

import java.util.Collection;
import java.util.Map;

import org.cucina.engine.definition.State;
import org.cucina.engine.definition.Token;
import org.cucina.engine.definition.Transition;
import org.cucina.engine.repository.WorkflowRepository;


/**
 * Main entry point into the workflow engine. Using a
 * <code>WorkflowSession</code> you can create a new workflow instance and
 * interact with existing workflow instances. <code>WorkflowSession</code>s are
 * created using a {@link ProcessSessionFactory}. Each
 * <code>WorkflowSession</code> is associated with a
 * <code>WorkflowDefinition</code> at creation time.
 * <p/>
 * Each invocation against the workflow engine has an associated
 * {@link ExecutionContext} which holds parameters relevant to that invocation.
 * You may choose to reuse the same {@link ExecutionContext} across many
 * invocations but this is a process-specific detail.
 * <p/>
 * Each workflow instance is associated with a single {@link PersistableObject
 * domain object}. You can later retrieve all workflow instances for a
 * particular {@link PersistableObject domain object} using a
 * {@link WorkflowRepository}.
 * <p/>
 * New workflow instances can be created thusly:
 *
 * <pre>
 * WorkflowSession session = getWorkflowSession();
 * PersistableObject domainObject = new MyDomainObject();
 * ExecutionContext executionContext = createExecutionContext();
 * session.startWorkflowInstance(executionContext, domainObject);
 * </pre>
 *
 * Once you have called {@link #startWorkflowInstance startWorkflowInstance} you
 * can retrieve the {@link WorkflowInstanceHandle} from the
 * {@link ExecutionContext} and store it for later use:
 *
 * <pre>
 * WorkflowInstanceHandle handle = executionContext.getHandle();
 * </pre>
 * <p/>
 * To continue an interaction with an existing workflow instance you need the
 * {@link WorkflowInstanceHandle}. In many cases, you may choose to store this
 * {@link WorkflowInstanceHandle} across user interactions so that it is easier
 * accessible whenever you are accessing the workflow engine. However, the size
 * of a {@link WorkflowInstanceHandle} may be too large to place in HTTP session
 * state or similar so you may choose to store only the
 * {@link WorkflowInstanceHandle#getId() handle ID} and use it to retrieve the
 * handle before accessing the workflow instance:
 *
 * <pre>
 * Long workflowInstanceId = getInstanceId();
 * session.retrieveHandle(workflowInstanceId);
 * </pre>
 * <p/>
 * Once you have retrieved the {@link WorkflowInstanceHandle} you can signal the
 * end of a state using one of the {@link #signal signal} methods:
 *
 * <pre>
 * ExecutionContext executionContext = createExecutionContext();
 * session.signal(executionContext);
 * </pre>
 *
 * @author Rob Harrop
 */
public interface ProcessSession {
    /**
     * JAVADOC Method Level Comments
     *
     * @param executionContext JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Gets all available {@link org.cucina.engine.definition.Transition
     * Transitions} out of the current state of the workflow instance identified
     * by the {@link WorkflowInstanceHandle} in the supplied
     * {@link ExecutionContext}.
     */
    Collection<Transition> getAvailableTransitions(ExecutionContext executionContext);

    /**
     * Creates execution context for an existing token
     *
     * @param token
     * @param parameters
     * @return
     */
    ExecutionContext createExecutionContext(Token token, Map<String, Object> parameters);

    /**
     * Signals the end of the current state for the workflow instance identified
     * in the {@link Token} in the supplied <code>ExecutionContext</code>. Exits
     * the state via the {@link org.cucina.engine.definition.Transition}
     * identified by the supplied ID.
     *
     * @see State#getTransition(String)
     */
    void signal(ExecutionContext executionContext, String transitionId)
        throws SignalFailedException;

    /**
     * Signals the end of the current state for the workflow instance identified
     * in the {@link Token} in the supplied <code>ExecutionContext</code>. Exits
     * the state via the supplied
     * {@link org.cucina.engine.definition.Transition}.
     */
    void signal(ExecutionContext executionContext, Transition transition)
        throws SignalFailedException;

    /**
     * Starts a new workflow instance for the supplied <code>domainObject</code>
     * . The <code>Token</code> associated with the newly created instance can
     * be accessed from the supplied <code>ExecutionContext</code>. Can also
     * provide a non required transitionId, if none is supplied the default
     * transition is used.
     */
    Token startWorkflowInstance(Object domainObject, String transitionId,
        Map<String, Object> parameters);
}
