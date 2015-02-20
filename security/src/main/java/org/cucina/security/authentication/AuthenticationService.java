
package org.cucina.security.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface AuthenticationService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param username JAVADOC.
     * @param password JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws AuthenticationException JAVADOC.
     */
    String authenticate(String username, String password)
        throws AuthenticationException;

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
