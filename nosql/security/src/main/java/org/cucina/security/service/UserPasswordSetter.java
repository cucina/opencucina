package org.cucina.security.service;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface UserPasswordSetter {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param username JAVADOC.
	 * @param password JAVADOC.
	 */
	void setUserPassword(String username, String password);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param username JAVADOC.
	 * @param password JAVADOC.
	 */
	void createAdminUser(String username, String password);
}
