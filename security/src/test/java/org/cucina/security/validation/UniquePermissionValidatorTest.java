package org.cucina.security.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.cucina.security.model.Permission;
import org.cucina.security.repository.PermissionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Tests that UserDaoUsernameValidatingPlugin functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UniquePermissionValidatorTest {
    private Permission permission;
    @Mock
    private PermissionRepository repo;
    private UniquePermissionValidator validator;

    /**
     * Permission invalid if exists
     */
    @Test
    public void permissionInvalid() {
        when(repo.exists(permission)).thenReturn(true);

        assertFalse("Should not be valid as permission is found",
            validator.isValid(permission, null));
    }

    /**
     * Permission valid if doesn't exist
     */
    @Test
    public void permissionValid() {
        when(repo.exists(permission)).thenReturn(false);

        assertTrue("Should be valid as permission is not found", validator.isValid(permission, null));
    }

    /**
     * Sets up for test
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        validator = new UniquePermissionValidator();
        validator.setPermissionRepository(repo);

        permission = new Permission();
    }
}
