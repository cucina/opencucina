package org.cucina.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


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
    Authentication authenticate(String username, String password)
        throws AuthenticationException;
}
