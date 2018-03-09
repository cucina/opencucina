package org.cucina.security.api;

import java.util.Collection;
import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 */
public interface AccessFacade {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 * @see WorkflowPermissionCriteriaBuilder
	 */
	String getDefaultPrivilege();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 * @see ProcessSupportServiceImpl
	 */
	String getSystemPrivilege();

	/**
	 * @param username  .
	 * @param privilege .
	 * @return .
	 */
	boolean hasPrivilege(String username, String privilege);

	/**
	 * @param username        .
	 * @param applicationType .
	 * @param properties      .
	 * @return .
	 */
	boolean hasPermissions(String username, String privilege, String applicationType, Map<String, Object> properties);

	/**
	 * @param username  .
	 * @param privilege .
	 * @return .
	 * @see WorkflowPermissionCriteriaBuilder
	 */
	Collection<PermissionDto> permissionsByUserAndPrivilege(String username, String privilege);

	/**
	 * @param username        .
	 * @param applicationType .
	 * @param accessLevel     .
	 * @return .
	 * @see SearchPermissionCriteriaBuilder
	 */
	Collection<PermissionDto> permissionsByUserTypeAccessLevel(String username,
															   String applicationType, String accessLevel);
}
