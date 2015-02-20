package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.repository.PermissionRepository;
import org.cucina.security.repository.RoleRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.util.Assert;


/**
 * Extension of <code>PropertyEditorSupport</code> to convert csv of <code>Role</code> names into <code>Permission</code>s.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RolesToPermissionsEditor
    extends PropertyEditorSupport {
    private PermissionRepository permissionRepository;
    private RoleRepository roleRepository;

    /**
     * Creates a new RolesEditor object.
     *
     * @param permissionDao JAVADOC.
     */
    public RolesToPermissionsEditor(PermissionRepository permissionRepository,
        RoleRepository roleRepository) {
        Assert.notNull(permissionRepository, "permissionRepository cannot be null");
        Assert.notNull(roleRepository, "roleRepository cannot be null");
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Converts csv roles into populated Role instances, loads from persistence.
     *
     * @param text String csv role names.
     *
     * @throws IllegalArgumentException.
     */
    @Override
    public void setAsText(String text)
        throws IllegalArgumentException {
        Collection<Permission> result = new HashSet<Permission>();

        if (StringUtils.isNotEmpty(text)) {
            String[] roleNames = text.split(",");

            for (int i = 0; i < roleNames.length; i++) {
                String roleName = roleNames[i].trim();

                Collection<Permission> permissions = permissionRepository.findByRoleName(roleName);
                Permission permission = null;

                if (CollectionUtils.isNotEmpty(permissions)) {
                    if (permissions.size() > 1) {
                        throw new IllegalArgumentException(roleName +
                            " returns more than 1 permission which is not expected.");
                    }

                    permission = permissions.iterator().next();
                }

                if (permission == null) {
                    //Load the role. 
                    Role role = roleRepository.findByName(roleName);

                    if (role != null) {
                        permission = new Permission();
                        permission.setName(roleName);
                        permission.setRole(role);
                    } else {
                        throw new OptimisticLockingFailureException("The role [" + roleName +
                            "] is not available");
                    }
                }

                result.add(permission);
            }
        }

        setValue(result);
    }
}
