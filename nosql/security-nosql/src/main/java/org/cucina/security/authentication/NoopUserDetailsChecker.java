
package org.cucina.security.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;


/**
 * Checker that does nothing
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class NoopUserDetailsChecker
    implements UserDetailsChecker {
    /**
     * No checks
     *
     * @param toCheck UserDetails.
     */
    @Override
    public void check(UserDetails toCheck) {
    }
}
