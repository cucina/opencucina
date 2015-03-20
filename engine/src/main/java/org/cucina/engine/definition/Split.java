package org.cucina.engine.definition;

import java.util.Collection;

import org.springframework.util.Assert;

import org.cucina.engine.CheckNotMetException;
import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;


/**
 * A {@link State} that splits the current path of execution in a workflow
 * instance into multiple parallel branches. <code>Split</code>s are usually
 * synchronized back into a single thread of execution using {@link Join Joins}.
 * <p/>
 * For each {@link Transition} on the a <code>Split</code> a new child
 * {@link Token} is spawned and sent over the {@link Transition}. The parent
 * {@link Token} moved to the null place until the execution path is
 * synchronized using a {@link Join}.
 *
 * @author Rob Harrop
 * @author vlevine
 * @see Join
 * @see Transition
 */
public class Split
    extends AbstractState {
    /**
     * Retrieves the default {@link Transition}.
     */
    @Override
    public Transition getDefaultTransition() {
        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param transition
     *            JAVADOC.
     */
    @Override
    public void addTransition(Transition transition) {
        Assert.isTrue(!transition.isDefault(), "Split cannot have default transitions");
        super.addTransition(transition);
    }

    /**
     * Upon entering, this {@link Split} immediately
     * {@link #leave(Transition, ExecutionContext) leaves}.
     *
     * @param from
     *            (ignored)
     * @param executionContext
     *            the current {@link ExecutionContext execution context}.
     */
    public void enter(Transition from, ExecutionContext executionContext) {
        super.enter(from, executionContext);
        leave((Transition) null, executionContext);
    }

    /**
     * Splits the current path of (workflow) execution into multiple, parallel
     * branches by spawning a new child execution for each {@link Transition}
     * contained within this {@link Split}.
     *
     * @param transition
     *            (ignored in the case of a {@link Split})
     * @param executionContext
     *            the current {@link ExecutionContext execution context}
     * @throws CheckNotMetException
     *             if not all of the {@link Check Conditions} attached to
     *             each of the {@link Transition Transitions} are met (c.f.
     *             {@link Transition#checkConditions(ExecutionContext)})
     */
    protected void leaveInternal(Transition transition, ExecutionContext executionContext) {
        Token parent = executionContext.getToken();
        Collection<Transition> transitions = getEnabledTransitions(executionContext);

        for (Transition splitTransition : transitions) {
            splitTransition.checkConditions(executionContext);
            split(executionContext, splitTransition, parent);
        }

        parent.setPlaceId(null);
    }

    /**
     * Splits the current execution path.
     *
     * @param executionContext
     *            the current (parent) {@link ExecutionContext execution
     *            context}.
     * @param newTransition
     *            the new (split) {@link Transition}.
     * @param parent
     *            the {@link Token parent Token}.
     */
    private void split(ExecutionContext executionContext, Transition newTransition, Token parent) {
        Token child = executionContext.getTokenFactory()
                                      .createToken(getProcessDefinition(), parent.getDomainObject());

        child.setPlaceId(this.getId());

        ExecutionContext wrappedContext = new DefaultExecutionContext(child, executionContext);

        // Conditions already passed, so it is safe to add the
        // child here - and necessary if the transition is straight
        // to a join.
        parent.addChild(child);
        newTransition.occur(wrappedContext);
    }
}
