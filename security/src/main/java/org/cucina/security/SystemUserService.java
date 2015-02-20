
package org.cucina.security;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface SystemUserService {
    /**
     * Gets either a pre-configured username to be used as a system user or one
     * of the list returned from persistence.
     *
     * @return username.
     */
    public String getUsername();
}
