package org.cucina.engine.definition;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;
import org.springframework.util.Assert;


/**
 * A {@link State} that acts as a synchronization point for multiple paths of
 * execution created by a {@link Split}.
 * <p>
 * <p>
 * To leave a <code>Join</code> all paths of execution spawned by the
 * {@link Split} must have ended and all child {@link Token Tokens} must now sit
 * on the <code>Join</code>.
 *
 * @author Rob Harrop
 * @author vlevine
 * @see Token
 * @see Split
 */
public class Join
		extends AbstractState {
	/**
	 * Indicates whether or not this {@link Join} can be left.
	 *
	 * @return <code>true</code> if the parent {@link Token} no longer has any
	 * children meaning that the {@link Split} has ended.
	 */
	public boolean canLeave(ExecutionContext executionContext) {
		Token token = executionContext.getToken();

		if (token.getParentToken() != null) {
			token = token.getParentToken();
		}

		return (!token.hasChildren());
	}

	/**
	 * JAVADOC
	 *
	 * @param from             JAVADOC
	 * @param executionContext JAVADOC
	 */
	public void enter(Transition from, ExecutionContext executionContext) {
		super.enter(from, executionContext);

		if (canLeave(executionContext)) {
			leave((Transition) null,
					new DefaultExecutionContext(executionContext.getToken().getParentToken(),
							executionContext));
		}
	}

	/**
	 * Checks to see if the parallel execution paths started by the
	 * {@link Split} have now ended and that all child {@link Token Tokens} now
	 * sit on this <code>Place</code>. If so, the parent {@link Token} is
	 * moved to this <code>Place</code> and all child {@link Token Tokens} are
	 * cleared out.
	 */
	protected void enterInternal(ExecutionContext executionContext) {
		super.enterInternal(executionContext);

		Token child = executionContext.getToken();
		Token parent = child.getParentToken();

		Assert.notNull(parent, "Parent token cannot be null when entering a Join");
		parent.removeChild(child);
	}

	/**
	 * JAVADOC.
	 *
	 * @param transition       JAVADOC.
	 * @param executionContext JAVADOC.
	 */
	protected void leaveInternal(Transition transition, ExecutionContext executionContext) {
		if (!canLeave(executionContext)) {
			return;
		}

		if (transition != null) {
			throw new IllegalArgumentException(
					"Cannot specify a transition when leaving a Join state.");
		}

		Token parent = executionContext.getToken();
		ExecutionContext parentExecContext = new DefaultExecutionContext(parent, executionContext);
		Transition exitTransition = getDefaultTransition();

		if (exitTransition.isEnabled(parentExecContext)) {
			fireLeaveActions(executionContext);
			exitTransition.occur(parentExecContext);

			return;
		}

		throw new IllegalStateException("Cannot leave Join state - no transitions are active.");
	}
}
