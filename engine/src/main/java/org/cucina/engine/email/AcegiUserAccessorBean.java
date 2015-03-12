
package org.cucina.engine.email;

import org.cucina.security.ContextUserAccessor;



/**
 * Acegi backed engineUserAccessor that gets current User
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AcegiUserAccessorBean
    implements UserAccessorBean {
    /**
     * Get current user from security context
     *
     * @return current User.
     */
    @Override
    public Object getCurrentUser() {
        return ContextUserAccessor.getCurrentUser();
    }
}
