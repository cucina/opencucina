
package org.cucina.engine.definition;

import java.util.Collection;

import org.cucina.engine.ExecutionContext;


/**
 * A <code>State</code> defines a particular state that a workflow instance
 * graph can sit in. A <code>State</code> may be a state, a split, a join or any
 * other state in the workflow that can be entered and left.
 * <p/>
 * Movement between <code>State</code>s is directed through {@link Transition
 * transitions}.
 *
 * @author vlevine
 * @see Transition
 */
public interface State
    extends ProcessElement, Cloneable {
    final String COLLECTION_EXPRESSION = "collectionExpression";

    /**
     * Property on <code>Place</code> indicating whether this state has the
     * default store/save transition
     */
    final String DEFAULT_STORE = "defaultStore";

    /**
     * Property on <code>Place</code> indicating whether this state is
     * delegateable or not
     */
    final String DELEGATEABLE = "delegateable";

    /**
     * Property on <code>Place</code> indicating whether an object in this state
     * may be edited
     */
    final String ENABLE_EDIT = "enableEdit";

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    /**
     * Gets all the {@link Transition Transitions} that can be used to leave
     * this <code>Place</code> given no specific workflow instance or
     * {@link org.cucina.engine.ExecutionContext}.
     *
     * @return JAVADOC.
     */
    Collection<Transition> getAllTransitions();

    /**
     * Gets the default {@link Transition Transition} for leaving this
     * <code>Place</code>.
     */
    Transition getDefaultTransition();

    /**
     * JAVADOC Method Level Comments
     *
     * @param executionContext JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Gets all enabled {@link Transition Transitions} that can be used to leave
     * this <code>Place</code> for a particular {@link ExecutionContext}. When
     * {@link org.cucina.engine.ProcessSession#signal signalling} the end
     * of a state using a given {@link ExecutionContext} only enabled
     * {@link Transition Transitions} can be used to leave this
     * <code>Place</code>.
     *
     * @param executionContext
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<Transition> getEnabledTransitions(ExecutionContext executionContext);

    /**
     * Gets a {@link Transition} that can be used to leave this
     * <code>Place</code> by identity.
     */
    Transition getTransition(String transitionId);

    /**
     * Indicates whether or not this <code>Place</code> is in a state that
     * allows it to be left. This test does not take into consideration any
     * {@link Check Conditions} that may be on the {@link Transition} used
     * to leave.
     */
    boolean canLeave(ExecutionContext executionContext);

    /**
     * Implements Object.clone() and should do a deep clone.
     *
     * @return a clone of this.
     */
    Object clone()
        throws CloneNotSupportedException;

    /**
     * Called when entering this <code>Place</code>. When entering this
     * <code>Place</code> after leaving another, the {@link Transition} used to
     * move between the two is supplied otherwise the supplied
     * {@link Transition} is <code>null</code>.
     */
    void enter(Transition from, ExecutionContext executionContext);

    /**
     * Indicates whether or not this <code>Place</code> has a {@link Transition}
     * identified by the specified ID.
     */
    boolean hasTransition(String transitionId);

    /**
     * Leaves this <code>Place</code> via the {@link Transition} specified by
     * the supplied ID.
     *
     * @see #getTransition(String)
     */
    void leave(String transitionId, ExecutionContext executionContext);

    /**
     * Leaves this <code>Place</code> via the supplied{@link Transition}.
     */
    void leave(Transition transition, ExecutionContext executionContext);
}
