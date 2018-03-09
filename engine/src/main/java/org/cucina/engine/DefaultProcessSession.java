package org.cucina.engine;

import org.cucina.engine.definition.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of {@link ProcessSession}. Instances of this class are
 * obtained from the {@link DefaultProcessSessionFactory}.
 *
 * @author Rob Harrop
 * @author vlevine
 * @see DefaultProcessSessionFactory
 * @see Token
 */
public class DefaultProcessSession
		implements ProcessSession {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultProcessSession.class);
	private static final String ERROR_CONTEXT_IS_REQUIRED = "Context is required.";
	private List<WorkflowListener> workflowListeners;

	/**
	 * The {@link ProcessDefinition} that this DefaultProcessSession is
	 * associated with.
	 */
	private ProcessDefinition workflowDefinition;
	private ProcessDriverFactory executorFactory;

	/**
	 * Creates a new <code>DefaultProcessSession</code> for the supplied
	 * {@link ProcessDefinition} and using the supplied {@link ProcessDriverFactory}.
	 */
	DefaultProcessSession(ProcessDefinition workflowDefinition,
						  ProcessDriverFactory executorFactory, List<WorkflowListener> workflowListeners) {
		Assert.notNull(workflowDefinition, "workflowDefinition is null");
		this.workflowDefinition = workflowDefinition;
		Assert.notNull(executorFactory, "executorFactory is null");
		this.executorFactory = executorFactory;
		this.workflowListeners = workflowListeners;
	}

	/**
	 * Gets all available {@link org.cucina.engine.definition.Transition
	 * Transitions} out of the current state of the workflow instance in the supplied {@link ExecutionContext}.
	 */
	public Collection<Transition> getAvailableTransitions(ExecutionContext executionContext) {
		Token token = getToken(executionContext);

		if (token.hasChildren()) {
			// this token does not have life of its own until all children are
			// dead.
			return Collections.emptyList();
		}

		State currentPlace = workflowDefinition.findPlace(token.getPlaceId());

		return currentPlace.getEnabledTransitions(executionContext);
	}

	/**
	 * Creates a {@link DefaultExecutionContext} to wrap the supplied
	 * {@link Token} and {@link ExecutionContext}.
	 */
	public ExecutionContext createExecutionContext(Token token, Map<String, Object> parameters) {
		return new DefaultExecutionContext(token, parameters, workflowListeners, executorFactory);
	}

	/**
	 * Delegates to {@link #doSignal}.
	 */
	public void signal(ExecutionContext executionContext, Transition transition) {
		Assert.notNull(executionContext, ERROR_CONTEXT_IS_REQUIRED);
		Assert.notNull(transition, "Cannot move to a null transition");

		doSignal(executionContext, transition);
	}

	/**
	 * Delegates to {@link #doSignal}.
	 */
	public void signal(ExecutionContext executionContext, String transitionId)
			throws SignalFailedException, TransitionNotFoundException {
		Assert.notNull(executionContext, ERROR_CONTEXT_IS_REQUIRED);

		signal(executionContext, findTransition(getToken(executionContext), transitionId));
	}

	/**
	 * Starts a new workflow instance for the supplied
	 * <code>PersistableObject</code>. The <code>WorkflowInstanceHandle</code>
	 * associated with the newly created instance can be accessed from the
	 * supplied <code>ExecutionContext</code>.
	 * <p/>
	 */
	public Token startProcessInstance(Object domainObject, String transitionId,
									  Map<String, Object> parameters)
			throws SignalFailedException, TransitionNotFoundException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Object=" + domainObject + " tokenFactory = " +
					executorFactory.getTokenFactory());
		}

		// Creating the handle. A token assigned to the same user as the
		// handle is also created
		// This token contains the start place.
		Token token = executorFactory.getTokenFactory().createToken(workflowDefinition, domainObject);

		ExecutionContext executionContext = createExecutionContext(token, parameters);

		// Processing the state
		State start = workflowDefinition.getStartState();

		start.enter(null, executionContext);
		start.leave(findTransition(token, transitionId), executionContext);

		return getToken(executionContext);
	}

	/**
	 * Gets the {@link Token} corresponding to the supplied
	 * {@link ExecutionContext}.
	 */
	private Token getToken(ExecutionContext executionContext) {
		return executionContext.getToken();
	}

	/**
	 * Leaves the input {@link State} of the specified {@link Transition}
	 * provided that {@link State} is a valid state of the current workflow
	 * instance.
	 */
	private void doSignal(ExecutionContext executionContext, Transition transition)
			throws CheckNotMetException, SignalFailedException {
		Token token = getToken(executionContext);

		Assert.notNull(token, "Null token in the executionContext");

		if (LOG.isDebugEnabled()) {
			LOG.debug("token = " + token);
		}

		State currentPlace = workflowDefinition.findPlace(token.getPlaceId());

		try {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Before leaving place " + token.getPlaceId() + " transition.getId()=" +
						transition.getId() + " transition.getOutput().getId()=" +
						transition.getOutput().getId());
			}

			currentPlace.leave(transition, executionContext);

			if (LOG.isDebugEnabled()) {
				LOG.debug("After leaving place id=" + token.getPlaceId());
			}
		} catch (CheckNotMetException ex) {
			throw ex;
		} catch (Exception e) {
			String id = "unknown";

			if (currentPlace != null) {
				id = token.getPlaceId();
			}

			throw new SignalFailedException("Unable to signal end of state [" + id +
					"]. See nested exception for more details", e);
		}
	}

	/**
	 * Finds a transition corresponding to the specified ID using the state of
	 * the supplied {@link Token}. If the {@link Token} has no children then the
	 * transition will be resolved against the current {@link State} of the
	 * {@link Token} itself. If the {@link Token} does have children then this
	 * method will search for the transition across the {@link State places}
	 * associated with the child {@link Token tokens}.
	 */
	private Transition findTransition(Token token, String transitionId)
			throws SignalFailedException, TransitionNotFoundException {
		return workflowDefinition.findPlace(token.getPlaceId()).getTransition(transitionId);
	}
}
