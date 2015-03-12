
package org.cucina.engine.definition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.engine.CheckNotMetException;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.WorkflowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.OrderComparator;


/**
 * A <code>Transition</code> provides a way of moving between {@link State
 * Places}.
 * <p/>
 * <code>Transition</code>s can have {@link Check Conditions} assigned to
 * them which can prevent them from occuring based on the data passed in via the
 * {@link org.cucina.engine.ExecutionContext}.
 * <p/>
 *
 * @author Rob Harrop
 * @author vlevine
 * @see #addCondition(Check)
 */
public class Transition
    extends AbstractWorkflowElement
    implements Cloneable, Serializable {
    private static final long serialVersionUID = 2527222175429217965L;
    private static final Logger LOG = LoggerFactory.getLogger(Transition.class);

    /** The exclusive privilege names securing this <code>Transition</code>. */
    private Collection<String> privilegeNames;

    /**
     * The {@link Check Conditions} to test before transitioning using this
     * <code>Transition</code>.
     */
    private transient List<Check> conditions = new ArrayList<Check>();

    /**
     * The {@link Operation Actions} that are fired just prior to this
     * {@link Transition} being ended.
     */
    private transient List<Operation> leaveActions = new ArrayList<Operation>();

    /** The {@link State} that is the input to this <code>Transition</code>. */
    private transient State input;

    /** The {@link State} that is the output from this <code>Transition</code>. */
    private transient State output;

    /** If this is the default transition */
    private boolean isDefault;

    /**
     * Indicates whether or not this <code>Transition</code> can be used to
     * leave the current {@link State}. Will return <code>false</code> if the
     * workflow instance identified by the supplied {@link ExecutionContext}
     * does not sit on the input {@link State} for this <code>Transition</code>.
     *
     * @see #addCondition(Check)
     * @see #getInput()
     */
    public final boolean isEnabled(ExecutionContext context) {
        if (!this.getInput().canLeave(context)) {
            return false;
        }

        return (findFirstFailingCondition(context) == null);
    }

    /**
     * Sets the {@link Operation Actions} that are fired just prior to this
     * {@link Transition} being ended.
     *
     * @param actions
     *            the {@link Operation Actions} that are fired just prior to this
     *            {@link Transition} being ended.
     */
    public void setActions(List<Operation> actions) {
        this.leaveActions = actions;
    }

    /**
     * Sets the {@link Check Conditions} to test before transitioning using
     * this {@link Transition}.
     *
     * @param conditions
     *            the {@link Check Conditions} to test before transitioning
     *            using this {@link Transition}..
     */
    public void setConditions(List<Check> conditions) {
        this.conditions = conditions;
    }

    /**
     * Gets the {@link Check Conditions} assigned to this {@link Transition}
     * . A {@link Transition} can only proceed if all its <code>Condition</code>
     * s evaluate to <code>true</code>.
     *
     * @return the {@link Check Conditions} assigned to this
     *         {@link Transition} (will never be <code>null</code>(.
     */
    public List<Check> getConditions() {
        if (conditions == null) {
            return Collections.emptyList();
        }

        return conditions;
    }

    /**
     * Set the default transition
     *
     * @param isDefault
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * is this the default transition
     *
     * @return
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Sets the {@link State} that is the input for this <code>Transition</code>
     * .
     */
    public void setInput(State input) {
        // TODO: validate allowed place types here
        this.input = input;
    }

    /**
     * Gets the {@link State} that is the input for this <code>Transition</code>
     * .
     */
    public State getInput() {
        return input;
    }

    /**
     * Sets the {@link State} that is the output for this
     * <code>Transition</code>. Ensures that the output {@link State} has the
     * correct {@link ProcessDefinition} associated with it.
     * <p/>
     * <strong>Note:</strong> this method must be called after
     * {@link #setInput(State) setting the input <code>Place</code>}.
     *
     * @throws IllegalStateException
     *             if the {@link #getInput() input place} has not yet been set
     * @see State#setWorkflowDefinition(ProcessDefinition)
     */
    public void setOutput(State output) {
        if (this.input == null) {
            throw new IllegalStateException(
                "Cannot associate a transition with an output place until the input place has been specified.");
        }

        // TODO: validate allowed Place types here
        this.output = output;
        this.output.setWorkflowDefinition(this.getWorkflowDefinition());
    }

    /**
     * Gets the {@link State} that is the output for this
     * <code>Transition</code>.
     */
    public State getOutput() {
        return output;
    }

    /**
     * Sets the privilege name required for this <code>Transition</code>.
     *
     * @param privilgeName
     *            String
     */
    public void setPrivilegeName(String privilegeName) {
        if (StringUtils.isNotEmpty(privilegeName)) {
            this.privilegeNames = Arrays.asList(StringUtils.stripAll(StringUtils.split(
                            privilegeName, ',')));
        } else {
            this.privilegeNames = null;
        }
    }

    /**
     * Gets the privilege name required for this <code>Transition</code>.
     */
    public Collection<String> getPrivilegeNames() {
        return privilegeNames;
    }

    /**
     * Adds a new {@link Check} to this <code>Transition</code>. A
     * <code>Transition</code> can only occur if <strong>all</strong></code>
     * {@link Check conditions} are met. Each {@link Check} can only be
     * added once to each <code>Transition</code>.
     */
    public void addCondition(Check condition) {
        this.conditions.add(condition);
        Collections.sort(conditions, new OrderComparator());
    }

    /**
     * Adds the supplied {@link Operation} to the {@link Operation Actions} that are
     * fired just prior to this {@link Transition} being ended.
     *
     * @param action
     *            the {@link Operation} to be so added.
     */
    public void addLeaveAction(Operation action) {
        leaveActions.add(action);
    }

    /**
     * Checks all {@link Check Conditions} on this <code>Transition</code>
     * and throws {@link org.cucina.engine.CheckNotMetException} when
     * it first encounters one that fails.
     */
    public void checkConditions(ExecutionContext executionContext)
        throws CheckNotMetException {
        Check failingCondition = findFirstFailingCondition(executionContext);

        if (failingCondition != null) {
            throw new CheckNotMetException("Unable to leave transition. Condition [" +
                failingCondition + "] not met.");
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     *
     * @throws CloneNotSupportedException JAVADOC.
     */
    @Override
    public Object clone()
        throws CloneNotSupportedException {
        Transition transition = (Transition) super.clone();
        State trOutput = (State) output.clone();

        transition.setOutput(trOutput);

        List<Check> trConditions = new ArrayList<Check>(conditions);

        transition.setConditions(trConditions);

        List<Operation> trActions = new ArrayList<Operation>(leaveActions);

        transition.setActions(trActions);

        return transition;
    }

    /**
     * Freezes this {@link Transition Transition's} state (i.e. makes this
     * {@link Transition} 'immune' to change.
     * <p>
     * This has the effect of making the collections underlying the
     * {@link #addCondition(Check)} and {@link #addLeaveAction(Operation)}
     * methods unmodifiable.
     */
    @SuppressWarnings("unchecked")
    public void immune() {
        leaveActions = ListUtils.unmodifiableList(leaveActions);
        conditions = ListUtils.unmodifiableList(conditions);
    }

    /**
     * Invokes this {@link Transition}. Called by
     * {@link org.cucina.engine.definition.State#leave}. Updates the
     * {@link Token} state so that the current {@link State} is
     * <code>null</code> and then enters the {@link #getOutput() output Place}.
     */
    public void occur(ExecutionContext executionContext) {
        Token token = executionContext.getToken();

        token.setPlaceId(null);

        if (this.output == null) {
            throw new IllegalStateException("Unable to end transition - no output Place specified.");
        }

        executionContext.getProcessDriver().execute(leaveActions, executionContext);

        if (LOG.isDebugEnabled()) {
            LOG.debug(getWorkflowDefinition().getId() + ": output place is " + this.output.getId());
        }

        List<WorkflowListener> workflowListeners = executionContext.getWorkflowListeners();

        if (workflowListeners != null) {
            for (WorkflowListener workflowListener : workflowListeners) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("LeavingState for " + workflowListener);
                }

                workflowListener.leavingState(input, this, executionContext);
            }
        }

        this.output.enter(this, executionContext);
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public String toString() {
        return new ToStringBuilder(this).append("id", getId())
                                        .append("input id", (input == null) ? null : input.getId())
                                        .append("output id",
            (output == null) ? null : output.getId()).append("conditions", conditions)
                                        .append("actions", leaveActions).toString();
    }

    /**
     * Finds the <b>first</b> {@link Check} assigned to this
     * {@link Transition} that fails and returns it. If no {@link Check}
     * fails then this method return <code>null</code>.
     *
     * @return the <b>first</b> failing {@link Check} , or <code>null</code>
     *         if none of the attached {@link Check Conditions} fails.
     */
    private Check findFirstFailingCondition(ExecutionContext executionContext) {
        if (conditions == null) {
            return null;
        }

        for (Check condition : conditions) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Condition is " + condition);
            }

            if (!executionContext.getProcessDriver().test(condition, executionContext)) {
                LOG.debug("Condition failed!");

                return condition;
            }
        }

        return null;
    }
}
