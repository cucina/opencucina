package org.cucina.security.service;

import org.cucina.security.model.User;


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
    User getCurrentUser();

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
    User forceUserToContext(String username);

    /**
     * To call from servlet directly
     *
     * @param username JAVADOC.
     * @param password JAVADOC.
     *
     * @return JAVADOC.
     */
    User forceUserToContext(String username, String password);
}
