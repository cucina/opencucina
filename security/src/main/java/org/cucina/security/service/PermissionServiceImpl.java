package org.cucina.security.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.cucina.security.api.PermissionDto;
import org.cucina.security.model.Permission;
import org.cucina.security.repository.PermissionRepository;


/**
 *
 *
 * @author vlevine
  */
@Service
public class PermissionServiceImpl
    implements PermissionService {
    @Autowired
    @Qualifier(value = "myConversionService")
    private ConversionService conversionService;
    @Autowired
    private PermissionRepository permissionRepository;

    /**
     *
     *
     * @param username .
     * @param privilegeName .
     *
     * @return .
     */
    @Override
    public Collection<PermissionDto> loadByUserAndPrivilege(String username, String privilegeName) {
        Collection<Permission> perms = permissionRepository.findByUserAndPrivilege(username,
                privilegeName);
        Collection<PermissionDto> result = new HashSet<PermissionDto>();

        for (Permission permission : perms) {
            result.add(conversionService.convert(permission, PermissionDto.class));
        }

        return result;
    }

    /**
     *
     *
     * @param username .
     * @param applicationType .
     * @param accessLevel .
     *
     * @return .
     */
    @Override
    public Collection<PermissionDto> loadByUserTypeAccessLevel(String username,
        String applicationType, String accessLevel) {
        // TODO Auto-generated method stub
        return null;
    }
}
