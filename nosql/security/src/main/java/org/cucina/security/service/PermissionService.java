package org.cucina.security.service;

import org.cucina.security.api.PermissionDto;

import java.util.Collection;


/**
 * @author vlevine
 */
public interface PermissionService {
	/**
	 * @param username  .
	 * @param privilege .
	 * @return .
	 */
	Collection<PermissionDto> loadByUserAndPrivilege(String username, String privilege);

	/**
	 * @param username        .
	 * @param applicationType .
	 * @param accessLevel     .
	 * @return .
	 */
	Collection<PermissionDto> loadByUserTypeAccessLevel(String username, String applicationType,
														String accessLevel);
}
