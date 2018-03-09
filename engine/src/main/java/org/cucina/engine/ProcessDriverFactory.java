package org.cucina.engine;

import org.cucina.core.spring.ExpressionExecutor;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ProcessDriverFactory {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	ProcessDriver getExecutor();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	ExpressionExecutor getExpressionExecutor();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	TokenFactory getTokenFactory();
}
