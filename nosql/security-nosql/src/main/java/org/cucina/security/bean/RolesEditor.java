package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.cucina.security.model.Role;
import org.cucina.security.repository.RoleRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.util.Assert;


/**
 * Extension of PropertyEditorSupport to convert csv of Role names into Roles.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RolesEditor
    extends PropertyEditorSupport {
    private RoleRepository roleRepository;

    /**
     * Creates a new RolesEditor object.
     *
     * @param permissionDao JAVADOC.
     */
    public RolesEditor(RoleRepository roleRepository) {
        Assert.notNull(roleRepository, "roleRepository cannot be null");
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
        Collection<Role> roles = new HashSet<Role>();

        if (StringUtils.isNotEmpty(text)) {
            String[] rolesNames = text.split(",");

            for (int i = 0; i < rolesNames.length; i++) {
                String roleName = rolesNames[i].trim();

                Role role = roleRepository.findByName(roleName);

                if (role == null) {
                    throw new OptimisticLockingFailureException("The role [" + roleName +
                        "] is not available");
                }

                roles.add(role);
            }
        }

        setValue(roles);
    }
}
