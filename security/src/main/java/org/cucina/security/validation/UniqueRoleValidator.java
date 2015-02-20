package org.cucina.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.cucina.security.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Tests to see if the Role name is unique
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniqueRoleValidator
    implements ConstraintValidator<UniqueRole, String> {
    private static final Logger LOG = LoggerFactory.getLogger(UniqueRoleValidator.class);
    private RoleRepository roleRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @param roleRepository JAVADOC.
     */
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Ensure that Role name is unique
     *
     * @param roleName String.
     * @param context ConstraintValidatorContext.
     *
     * @return true if role doesn't exist.
     */
    @Override
    public boolean isValid(String roleName, ConstraintValidatorContext context) {
        if (roleRepository == null) {
            LOG.info("roleRepository is not wired, returning true");

            return true;
        }

        return !roleRepository.exists(roleName);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0 JAVADOC.
     */
    @Override
    public void initialize(UniqueRole arg0) {
        //Not sure what to do here
    }
}
