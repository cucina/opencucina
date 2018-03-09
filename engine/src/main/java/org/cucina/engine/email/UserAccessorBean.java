package org.cucina.engine.email;


/**
 * User accessor methods to be implemented by third party clients.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface UserAccessorBean {
	/**
	 * Get the currently logged in User
	 *
	 * @return engineUser.
	 */
	Object getCurrentUser();
}
