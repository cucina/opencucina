package org.cucina.security.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import org.cucina.security.api.PermissionDto;
import org.cucina.security.model.Permission;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;


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
    private UserRepository userRepository;

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
        Collection<User> users = userRepository.findByPermissionsRolePrivilegesName(privilegeName);

        Collection<PermissionDto> result = new HashSet<PermissionDto>();

        for (User user : users) {
            if (user.getName().equals(username)) {
                for (Permission permission : user.getPermissions()) {
                    result.add(conversionService.convert(permission, PermissionDto.class));
                }
            }
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
