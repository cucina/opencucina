package org.cucina.security.access;

import org.cucina.security.model.Privilege;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface AccessRegistry {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Privilege getDefaultPrivilege();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Privilege getSystemPrivilege();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationType JAVADOC.
	 * @param accessLevel     JAVADOC.
	 * @return JAVADOC.
	 */
	public Privilege lookup(String applicationType, String accessLevel);
}
