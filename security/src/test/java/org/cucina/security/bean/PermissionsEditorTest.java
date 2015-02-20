package org.cucina.security.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.cucina.security.model.Permission;
import org.cucina.security.repository.PermissionRepository;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class PermissionsEditorTest {
    private static final Long PERMISSION_ID1 = 1L;
    private static final Long PERMISSION_ID2 = 2L;
    private Permission permission1;
    private Permission permission2;
    @Mock
    private PermissionRepository permissionRepository;
    private PermissionsEditor editor;

    /**
     * If empty text is provided does nothing
     */
    @Test
    public void emptyText() {
        editor.setAsText("");

        @SuppressWarnings("unchecked")
        Collection<Permission> roles = (Collection<Permission>) editor.getValue();

        assertNotNull("Should return collection of roles", roles);
        assertEquals("Incorrect number roles", 0, roles.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsPermissions() {
        when(permissionRepository.find(PERMISSION_ID1)).thenReturn(permission1);
        when(permissionRepository.find(PERMISSION_ID2)).thenReturn(permission2);

        editor.setAsText(PERMISSION_ID1 + "," + PERMISSION_ID2);

        @SuppressWarnings("unchecked")
        Collection<Permission> roles = (Collection<Permission>) editor.getValue();

        assertNotNull("Should return collection of roles", roles);
        assertEquals("Incorrect number roles", 2, roles.size());
        assertTrue("Should contain permission1", roles.contains(permission1));
        assertTrue("Should contain permission2", roles.contains(permission2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsPermissionsWithWhitespace() {
        when(permissionRepository.find(PERMISSION_ID1)).thenReturn(permission1);
        when(permissionRepository.find(PERMISSION_ID2)).thenReturn(permission2);

        editor.setAsText(PERMISSION_ID1 + " , " + PERMISSION_ID2);

        @SuppressWarnings("unchecked")
        Collection<Permission> roles = (Collection<Permission>) editor.getValue();

        assertNotNull("Should return collection of roles", roles);
        assertEquals("Incorrect number roles", 2, roles.size());
        assertTrue("Should contain role1", roles.contains(permission1));
        assertTrue("Should contain role2", roles.contains(permission2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        MockitoAnnotations.initMocks(this);
        LoggingEnabler.enableLog(PermissionsEditor.class);
        editor = new PermissionsEditor(permissionRepository);
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
    @Test(expected = IllegalArgumentException.class)
    public void roleNoLongerExists() {
        when(permissionRepository.find(PERMISSION_ID1)).thenReturn(null);

        editor.setAsText(PERMISSION_ID1 + "," + PERMISSION_ID2);
    }
}
