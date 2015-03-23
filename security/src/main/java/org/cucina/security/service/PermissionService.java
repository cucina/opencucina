package org.cucina.security.service;

import java.util.Collection;

import org.cucina.security.api.PermissionDto;

public interface PermissionService {
	Collection<PermissionDto> loadByUserAndPrivilege(String username,
            String privilege);

	Collection<PermissionDto> loadByUserTypeAccessLevel(String username, String applicationType,
			String accessLevel);
}
