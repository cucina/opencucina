package org.cucina.security.validation;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface UsernameValidatingPlugin {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param username JAVADOC.
	 * @return JAVADOC.
	 */
	boolean isValid(String username);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	String message();
}
