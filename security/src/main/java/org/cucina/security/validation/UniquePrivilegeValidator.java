package org.cucina.security.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.cucina.security.repository.PrivilegeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Tests to see if the Privilege name is unique
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniquePrivilegeValidator
    implements ConstraintValidator<UniquePrivilege, String> {
    private static final Logger LOG = LoggerFactory.getLogger(UniquePrivilegeValidator.class);
    private PrivilegeRepository privilegeRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @param permissionDao JAVADOC.
     */
    @Autowired
    public void setPrivilegeRepository(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    /**
     * Ensure that Privilege name is unique
     *
     * @param privName String.
     * @param context ConstraintValidatorContext.
     *
     * @return true if role doesn't exist.
     */
    @Override
    public boolean isValid(String privName, ConstraintValidatorContext context) {
        if (privilegeRepository == null) {
            LOG.info("privilegeRepository is not wired, returning true");

            return true;
        }

        return !privilegeRepository.exists(privName);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param arg0 JAVADOC.
     */
    @Override
    public void initialize(UniquePrivilege arg0) {
        //Not sure what to do here
    }
}
