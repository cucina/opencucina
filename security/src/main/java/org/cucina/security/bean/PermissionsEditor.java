package org.cucina.security.bean;

import java.beans.PropertyEditorSupport;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.cucina.security.model.Permission;
import org.cucina.security.repository.PermissionRepository;
import org.springframework.util.Assert;


/**
 * Extension of PropertyEditorSupport to convert csv of Permission ids into <code>Permission</code>s.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PermissionsEditor
    extends PropertyEditorSupport {
    private PermissionRepository permissionRepository;

    /**
     * Creates a new PermissionsEditor object.
     *
     * @param persistenceService used to load <code>Permission</code>s.
     */
    public PermissionsEditor(PermissionRepository permissionRepository) {
        Assert.notNull(permissionRepository, "permissionRepository cannot be null");
        this.permissionRepository = permissionRepository;
    }

    /**
     * Converts csv ids into populated Permission instances, loads from persistence.
     *
     * @param text String csv Permission id.
     *
     * @throws IllegalArgumentException.
     */
    @Override
    public void setAsText(String text)
        throws IllegalArgumentException {
        Collection<Permission> permissions = new HashSet<Permission>();

        if (StringUtils.isNotEmpty(text)) {
            String[] permissionIds = text.split(",");

            for (int i = 0; i < permissionIds.length; i++) {
                String permissionId = permissionIds[i].trim();

                Permission permission = permissionRepository.find(Long.valueOf(permissionId));

                if (permission == null) {
                    throw new IllegalArgumentException("The permission [" + permissionId +
                        "] is not available");
                }

                permissions.add(permission);
            }
        }

        setValue(permissions);
    }
}
