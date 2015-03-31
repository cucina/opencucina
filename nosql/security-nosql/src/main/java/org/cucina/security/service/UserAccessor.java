package org.cucina.security.service;

import org.springframework.security.core.userdetails.UserDetails;


/**
 * Gets and sets current user in the execution context.
 *
 * @author $Author: $
  */
public interface UserAccessor {
    /**
     * Uses the persistence service to obtain the most up-to-date version of
     * the currently logged-in user.
     *
     * @return JAVADOC.
     */
    UserDetails getCurrentUser();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    boolean isSso();

	/**
	 * Inside job - for interceptor
	 *
	 * @param username JAVADOC.
	 * @param token JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	UserDetails forceUserToContext(String username);

	/**
	 * To call from servlet directly
	 *
	 * @param username JAVADOC.
	 * @param password JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	UserDetails forceUserToContext(String username, String password);
}
