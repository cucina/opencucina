package org.cucina.engine.definition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.WorkflowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * Convenience implementation of the {@link State} interface that provides
 * implementations for shared methods and provides base implementations for
 * entering and leaving that take care of firing {@link Operation Actions}.
 * <p>
 * Subclasses should override {@link #enterInternal enterInternal} and
 * {@link #leaveInternal leaveInternal} to implement any custom behavior.
 *
 * @author Rob Harrop
 * @see Transition
 */
public abstract class AbstractState
    extends AbstractWorkflowElement
    implements State {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractState.class);

    /**
     * Stores all the {@link Transition Transitions} out of this {@link State}.
     */
    private List<Transition> allTransitions = new ArrayList<Transition>();

    /**
     * Stores all the enter {@link Operation Actions} for this {@link State}.
     */
    private List<Operation> enterActions = new ArrayList<Operation>();

    /**
     * Stores all the exit {@link Operation Actions} for this {@link State}.
     */
    private List<Operation> leaveActions = new ArrayList<Operation>();

    /**
     * Stores all the {@link Transition Transitions} out of this {@link State}
     * keyed by their ID.
     */
    private Map<String, Transition> keyedTransitions = new HashMap<String, Transition>();

    /**
     * Called to leave this <code>Place</code>. Delegates to the
     * {@link #leaveInternal leaveInternal} method and then fires all leaving
     * {@link Operation Actions}. Subclasses should customise leave behaviour by
     * overriding the {@link #leaveInternal leaveInternal} method.
     *
     * @see #leaveInternal(Transition, ExecutionContext)
     */
    public final void leave(String transitionId, ExecutionContext executionContext) {
        Transition transition = getTransition(transitionId);

        leave(transition, executionContext);
    }

    /**
     * Called to leave this <code>Place</code>. Delegates to the
     * {@link #leaveInternal leaveInternal} method and then fires all leaving
     * {@link Operation Actions}. Subclasses should customise leave behaviour by
     * overriding the {@link #leaveInternal leaveInternal} method.
     *
     * @see #leaveInternal(Transition, ExecutionContext)
     */
    public final void leave(Transition transition, ExecutionContext executionContext) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(((getWorkflowDefinition() == null) ? "" : getWorkflowDefinition().getId()) +
                ": Leaving " + this.getId() + " using transition '" +
                ((transition == null) ? null : transition.getId()) + "'");
        }

        leaveInternal(transition, executionContext);
    }

    /**
     * Gets all the {@link Transition Transitions} from this <code>Place</code>.
     */
    @Override
    public Collection<Transition> getAllTransitions() {
        return allTransitions;
    }

    /**
     * Gets the default {@link Transition} used to leave this <code>Place</code>
     * .
     *
     * @throws TransitionNotFoundException
     *             if no default {@link Transition} can be found.
     */
    public Transition getDefaultTransition() {
        Transition transition = getDefaultTransitionInternal();

        if (transition != null) {
            return transition;
        }

        throw new TransitionNotFoundException("Unable to locate default transition.");
    }

    /**
     * Gets all {@link Transition#isEnabled enabled Transitions} that can be
     * used to leave this <code>Place</code>.
     */
    public Collection<Transition> getEnabledTransitions(ExecutionContext executionContext) {
        List<Transition> result = new ArrayList<Transition>();

        for (Transition transition : allTransitions) {
            if (transition.isEnabled(executionContext)) {
                result.add(transition);
            }
        }

        return result;
    }

    /**
    * Gets a {@link Transition} that can be used to leave this
    * <code>Place</code> by identity.
    */
    public Transition getTransition(String transitionId) {
        Transition transition = this.keyedTransitions.get(transitionId);

        if (transition == null) {
            throw new TransitionNotFoundException("Transition [" + transitionId + "] not found.");
        }

        return transition;
    }

    /**
     * Stores the {@link ProcessDefinition} for this <code>Place</code> and
     * registers this <code>Place</code> with that {@link ProcessDefinition}.
     */
    public void setWorkflowDefinition(ProcessDefinition workflowDefinition) {
        super.setWorkflowDefinition(workflowDefinition);
        getWorkflowDefinition().registerPlace(this);
    }

    /**
     * Adds a new enter {@link Operation} to this <code>Place</code>.
     */
    public void addEnterAction(Operation action) {
        this.enterActions.add(action);
    }

    /**
     * Adds a new leave {@link Operation} to this <code>Place</code>.
     */
    public void addLeaveAction(Operation action) {
        this.leaveActions.add(action);
    }

    /**
     * Adds a new {@link Transition} to this <code>Place</code>.
     */
    public void addTransition(Transition transition) {
        // If default transition check we haven't already got one
        if (transition.isDefault()) {
            Assert.isNull(getDefaultTransitionInternal(),
                "Cannot add default transition with id [" + transition.getId() +
                "] as there is already one.");
        }

        this.allTransitions.add(transition);
        this.keyedTransitions.put(transition.getId(), transition);
        transition.setWorkflowDefinition(getWorkflowDefinition());
        transition.setInput(this);
    }

    /**
     * Indicates whether, given the supplied {@link ExecutionContext} this
     * <code>Place</code> can be left. Does not take into consideration whether
     * the {@link Transition transitions} associated with this
     * <code>Place</code> are active or not.
     * <p/>
     * Default implementation simply checks to see whether the {@link Token} for
     * the supplied {@link ExecutionContext} sits at this <code>Place</code>.
     *
     * @see #leave(ExecutionContext)
     */
    public boolean canLeave(ExecutionContext executionContext) {
        String pl = executionContext.getToken().getPlaceId();

        return (this.getId().equals(pl));
    }

    /**
     * @return a clone of this instance.
     * @see Object#clone()
     */
    public Object clone()
        throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Called when entering this <code>Place</code>. Delegates to the
     * {@link #enterInternal enterInternal} method and then fires all entry
     * {@link Operation Actions}. Subclasses should customise entry behaviour by
     * overriding the {@link #enterInternal enterInternal} method.
     *
     * @see #enterInternal(ExecutionContext)
     */
    public void enter(Transition from, ExecutionContext executionContext) {
        enterInternal(executionContext);
        fireActions(this.enterActions, executionContext);

        List<WorkflowListener> listeners = executionContext.getWorkflowListeners();

        if (listeners != null) {
            for (WorkflowListener workflowListener : listeners) {
                workflowListener.enteredState(this, from, executionContext);
            }
        }
    }

    /**
     * Returns <code>true</code> if this <code>Place</code> has a
     * {@link Transition} with the supplied ID, otherwise returns
     * <code>false</code>.
     */
    public boolean hasTransition(String transitionId) {
        return (this.keyedTransitions.containsKey(transitionId));
    }

    /**
     * Freezes this {@link AbstractState AbstractPlace's} state (i.e. makes this
     * {@link AbstractState} 'immune' to change.
     */
    @SuppressWarnings("unchecked")
    public void immune() {
        allTransitions = ListUtils.unmodifiableList(allTransitions);
        keyedTransitions = MapUtils.unmodifiableMap(keyedTransitions);
        enterActions = ListUtils.unmodifiableList(enterActions);
        leaveActions = ListUtils.unmodifiableList(leaveActions);
    }

    /**
     * @return a {@link String} representation of this object
     * @see Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Fired when entering this <code>Place</code>. Default implementation
     * simply updates the current place of {@link Token} in the supplied
     */
    protected void enterInternal(ExecutionContext executionContext) {
        Assert.notNull(executionContext, "ExecutionContext cannot be null.");

        Token token = executionContext.getToken();

        Assert.notNull(token, "Token cannot be null");

        token.setPlaceId(this.getId());
    }

    /**
     * Executes all configured leave {@link Operation Actions}.
     *
     */
    protected void fireLeaveActions(ExecutionContext executionContext) {
        fireActions(this.leaveActions, executionContext);
    }

    /**
     * Fired when leaving this <code>Place</code>. Default implementation simply
     * checks that the supplied {@link Transition} passes all conditions and
     * then calls
     * {@link org.cucina.engine.definition.Transition#occur}.
     */
    protected void leaveInternal(Transition transition, ExecutionContext executionContext) {
        if (!canLeave(executionContext)) {
            throw new IllegalArgumentException("Cannot leave current Place '" + this.getId() +
                "' since it is not the active place associated with the supplied ExecutionContext");
        }

        if (transition == null) {
            throw new IllegalArgumentException("Transition cannot be null");
        }

        transition.checkConditions(executionContext);
        fireLeaveActions(executionContext);
        transition.occur(executionContext);
    }

    private Transition getDefaultTransitionInternal() {
        if (this.allTransitions.size() > 0) {
            for (Transition transition : allTransitions) {
                if (transition.isDefault()) {
                    return transition;
                }
            }
        }

        return null;
    }

    /**
     * Iterates over the <code>List</code> of supplied {@link Operation Actions}
     * and executes each one in sequence passing in the supplied
     */
    private void fireActions(List<Operation> actions, ExecutionContext executionContext) {
        executionContext.getProcessDriver().execute(actions, executionContext);
    }
}
