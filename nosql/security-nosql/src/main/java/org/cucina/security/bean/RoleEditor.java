package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang3.StringUtils;
import org.cucina.security.model.Role;
import org.cucina.security.repository.RoleRepository;
import org.springframework.util.Assert;


/**
 * Extension of PropertyEditorSupport to convert Role name into Role.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RoleEditor
    extends PropertyEditorSupport {
    private RoleRepository roleRepository;

    /**
     * Creates a new RolesEditor object.
     *
     * @param permissionDao JAVADOC.
     */
    public RoleEditor(RoleRepository roleRepository) {
        Assert.notNull(roleRepository, "roleRepository cannot be null");
        this.roleRepository = roleRepository;
    }

    /**
     * Converts role name into populated Role instance, loads from persistence.
     *
     * @param text String csv role names.
     *
     * @throws IllegalArgumentException.
     */
    @Override
    public void setAsText(String text)
        throws IllegalArgumentException {
        Role role = null;

        if (StringUtils.isNotEmpty(text)) {
            String roleName = text.trim();

            role = roleRepository.findByName(roleName);

            if (role == null) {
                throw new IllegalArgumentException("The role [" + roleName + "] is not available");
            }
        }

        setValue(role);
    }
}
