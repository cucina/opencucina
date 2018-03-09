package org.cucina.engine.definition;

import org.cucina.engine.DefaultExecutionContext;
import org.cucina.engine.ExecutionContext;
import org.cucina.engine.SignalFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Collection;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision$
 */
public class CollectionSplit
		extends AbstractState {
	private static Logger LOG = LoggerFactory.getLogger(CollectionSplit.class);
	private String collectionExpression;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param collectionExpression JAVADOC.
	 */
	public void setCollectionExpression(String collectionExpression) {
		this.collectionExpression = collectionExpression;
	}

	/**
	 * Allows only for a single transition
	 *
	 * @param transition JAVADOC.
	 */
	@Override
	public void addTransition(Transition transition) {
		Assert.isTrue(transition.isDefault(), "CollectionSplit can only have default transition");
		super.addTransition(transition);
	}

	/**
	 * Enter and leave immediately
	 *
	 * @param from             JAVADOC
	 * @param executionContext JAVADOC
	 */
	public void enter(Transition from, ExecutionContext executionContext) {
		Assert.notNull(collectionExpression,
				"CollectionExpression has not been set to non-null value");
		super.enter(from, executionContext);
		Assert.notNull(getDefaultTransition(), "No transitions were set for this CollectionSplit");
		leave(getDefaultTransition(), executionContext);
	}

	/**
	 * Splits workflow for parallel execution on members of a collection. The
	 * collection is identified by <code>collectionExpression</code> parameter
	 * in execution context's parameters. For example, expression could look
	 * like <code>token.domainObject.children</code> fetching collection
	 * <code>children</code>. The passed in transition is used for each member
	 * of the collection.
	 *
	 * @param transition       JAVADOC.
	 * @param executionContext JAVADOC.
	 */
	protected void leaveInternal(Transition transition, ExecutionContext executionContext) {
		Token token = executionContext.getToken();

		if (LOG.isDebugEnabled()) {
			LOG.debug("COLLECTION SPLIT:leaveInternal");
		}

		@SuppressWarnings("unchecked")
		Collection<Object> coll = (Collection<Object>) executionContext.getExpressionExecutor()
				.evaluate(executionContext,
						collectionExpression);

		Assert.notEmpty(coll, "A split cannot be performed on an empty collection");

		for (Object element : coll) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("creating token for child " + element);
			}

			// spawn a child token
			Token child = executionContext.getTokenFactory()
					.createToken(getProcessDefinition(), element);

			child.setPlaceId(this.getId());

			ExecutionContext childContext = new DefaultExecutionContext(child, executionContext);
			Transition splitTransition;

			try {
				splitTransition = (Transition) transition.clone();
			} catch (CloneNotSupportedException e) {
				throw new SignalFailedException("Failed to clone transition or one of its properties",
						e);
			}

			splitTransition.checkConditions(childContext);
			splitTransition.occur(childContext);
			token.addChild(child);
		}
	}
}
