package org.cucina.security.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;

import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.repository.PermissionRepository;
import org.cucina.security.repository.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.OptimisticLockingFailureException;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RolesToPermissionsEditorTest {
    private static final String ROLENAME1 = "role1";
    private static final String ROLENAME2 = "role2";
    private Permission permission1;
    private Permission permission2;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private RoleRepository roleRepository;
    private RolesToPermissionsEditor editor;

    /**
     * Test that if there isn't currently
     */
    @Test
    public void createsPermissionFromRole() {
        when(permissionRepository.findByRoleName(ROLENAME1))
            .thenReturn(Collections.singleton(permission1));
        when(permissionRepository.findByRoleName(ROLENAME2))
            .thenReturn(Collections.<Permission>emptySet());

        Role role = new Role();

        when(roleRepository.findByName(ROLENAME2)).thenReturn(role);
        editor.setAsText(ROLENAME1 + "," + ROLENAME2);

        @SuppressWarnings("unchecked")
        Collection<Permission> permissions = (Collection<Permission>) editor.getValue();

        assertNotNull("Should return collection of permissions", permissions);
        assertEquals("Incorrect number roles", 2, permissions.size());
        assertTrue("Should contain permission1", permissions.contains(permission1));

        verify(permissionRepository).findByRoleName(ROLENAME1);
        verify(permissionRepository).findByRoleName(ROLENAME2);
    }

    /**
     * If empty text is provided does nothing
     */
    @Test
    public void emptyText() {
        editor.setAsText("");

        @SuppressWarnings("unchecked")
        Collection<Permission> permissions = (Collection<Permission>) editor.getValue();

        assertNotNull("Should return collection of permissions", permissions);
        assertEquals("Incorrect number permissions", 0, permissions.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsPermissions() {
        when(permissionRepository.findByRoleName(ROLENAME1))
            .thenReturn(Collections.singleton(permission1));
        when(permissionRepository.findByRoleName(ROLENAME2))
            .thenReturn(Collections.singleton(permission2));

        editor.setAsText(ROLENAME1 + "," + ROLENAME2);

        @SuppressWarnings("unchecked")
        Collection<Permission> permissions = (Collection<Permission>) editor.getValue();

        assertNotNull("Should return collection of permissions", permissions);
        assertEquals("Incorrect number roles", 2, permissions.size());
        assertTrue("Should contain permission1", permissions.contains(permission1));
        assertTrue("Should contain permission2", permissions.contains(permission2));

        verify(permissionRepository).findByRoleName(ROLENAME1);
        verify(permissionRepository).findByRoleName(ROLENAME2);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsPermissionsWithWhitespace() {
        when(permissionRepository.findByRoleName(ROLENAME1))
            .thenReturn(Collections.singleton(permission1));
        when(permissionRepository.findByRoleName(ROLENAME2))
            .thenReturn(Collections.singleton(permission2));

        editor.setAsText(ROLENAME1 + " , " + ROLENAME2);

        @SuppressWarnings("unchecked")
        Collection<Permission> permissions = (Collection<Permission>) editor.getValue();

        assertNotNull("Should return collection of roles", permissions);
        assertEquals("Incorrect number roles", 2, permissions.size());
        assertTrue("Should contain role1", permissions.contains(permission1));
        assertTrue("Should contain role2", permissions.contains(permission2));

        verify(permissionRepository).findByRoleName(ROLENAME1);
        verify(permissionRepository).findByRoleName(ROLENAME2);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        MockitoAnnotations.initMocks(this);
        editor = new RolesToPermissionsEditor(permissionRepository, roleRepository);
        permission1 = new Permission();
        permission1.setId(11L);
        permission1.setName("bla");

        permission2 = new Permission();
        permission2.setId(12L);
        permission2.setName("blabla");
    }

    /**
     * If
     */
    @Test(expected = OptimisticLockingFailureException.class)
    public void roleNoLongerExists() {
        when(permissionRepository.findByRoleName(ROLENAME1))
            .thenReturn(Collections.<Permission>emptySet());
        when(roleRepository.findByName(ROLENAME1)).thenReturn(null);

        editor.setAsText(ROLENAME1 + "," + ROLENAME2);

        verify(permissionRepository).findByRoleName(ROLENAME1);
    }
}
