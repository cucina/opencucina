package org.cucina.security.service;

import java.util.Collection;

import org.cucina.security.api.PermissionDto;


/**
 * 
 *
 * @author vlevine
  */
public interface PermissionService {
    /**
     *
     *
     * @param username .
     * @param privilege .
     *
     * @return .
     */
    Collection<PermissionDto> loadByUserAndPrivilege(String username, String privilege);

    /**
     *
     *
     * @param username .
     * @param applicationType .
     * @param accessLevel .
     *
     * @return .
     */
    Collection<PermissionDto> loadByUserTypeAccessLevel(String username, String applicationType,
        String accessLevel);
}
