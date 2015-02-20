
package org.cucina.security.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LocalAuthenticationService
    implements AuthenticationService {
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
    @Override
    public String authenticate(String username, String password)
        throws AuthenticationException {
        return username;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public UserDetails forceUserToContext(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username JAVADOC.
     * @param password JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public UserDetails forceUserToContext(String username, String password) {
        // TODO Auto-generated method stub
        return null;
    }
}
