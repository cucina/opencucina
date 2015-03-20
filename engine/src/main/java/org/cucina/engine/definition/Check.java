
package org.cucina.engine.definition;

import org.cucina.engine.ExecutionContext;


/**
 * A <code>Condition</code> is a simple mechanism for limiting access to a
 * resource or making a decision on which action to take. <code>Condition</code>s
 * are evaluated against an {@link ExecutionContext} using the
 * {@link #test} method. <p/> Within the network, <code>Condition</code>s are
 * used to limit access to {@link Transition Transitions} or to make a choice
 * about which {@link Transition} to use. The exact usage of
 * <code>Condition</code>s in relation to {@link Transition Transitions} is
 * {@link State} implementation specific.
 *
 * @author Rob Harrop
 * @see Operation
 * @see Transition
 */
public interface Check
    extends ProcessElement {
    /**
     * Test this <code>Condition</code> against the supplied
     * {@link ExecutionContext}. Should return <code>true</code> if
     * <code>Condition</code> passes otherwise <code>false</code>.
     */
    boolean test(ExecutionContext executionContext);
}
