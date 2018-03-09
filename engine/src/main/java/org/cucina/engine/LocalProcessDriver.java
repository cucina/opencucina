package org.cucina.engine;

import org.cucina.engine.definition.Check;
import org.cucina.engine.definition.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * The class for embedded workflow, executes actions and conditions locally.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class LocalProcessDriver
		implements ProcessDriver {
	private static final Logger LOG = LoggerFactory.getLogger(LocalProcessDriver.class);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param actions          JAVADOC.
	 * @param executionContext JAVADOC.
	 */
	@Override
	public void execute(List<Operation> actions, ExecutionContext executionContext) {
		for (Operation action : actions) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Executing action:" + action);
			}

			action.execute(executionContext);
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param condition        JAVADOC.
	 * @param executionContext JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean test(Check condition, ExecutionContext executionContext) {
		return condition.test(executionContext);
	}
}
