package org.cucina.security.authentication;

import org.springframework.security.core.AuthenticationException;


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
}
