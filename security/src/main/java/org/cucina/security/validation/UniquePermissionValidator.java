package org.cucina.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.cucina.security.model.Permission;
import org.cucina.security.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniquePermissionValidator
    implements ConstraintValidator<UniquePermission, Permission> {
    private PermissionRepository permissionRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @param messageDao JAVADOC.
     */
    @Autowired
    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNode JAVADOC.
     * @param arg1 JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isValid(Permission permission, ConstraintValidatorContext arg1) {
        if (permissionRepository == null) {
            return true;
        }

        return !permissionRepository.exists(permission);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param uniqueMessageCode JAVADOC.
     */
    @Override
    public void initialize(UniquePermission uniquePermission) {
    }
}
