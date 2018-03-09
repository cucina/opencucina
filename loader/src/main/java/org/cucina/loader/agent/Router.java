package org.cucina.loader.agent;

/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface Router extends Agent {
	/**
	 * If this method returns true, do nothing further (ie if this is being executed as one of a stack, proceed to next in the stack)
	 * <p>
	 * If this method returns false, route to alternative executor
	 *
	 * @return
	 */
	boolean route();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	void runAlternative();
}
