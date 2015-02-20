package org.cucina.security.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.cucina.security.model.Role;
import org.cucina.security.repository.RoleRepository;
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
public class RoleEditorTest {
    private static final String ROLE1_NAME = "role1";
    private static final String ROLE2_NAME = "role2";
    private Role role1;
    private Role role2;
    private RoleEditor editor;
    @Mock
    private RoleRepository roleRepository;

    /**
     * If empty text is provided does nothing
     */
    @Test
    public void emptyText() {
        editor.setAsText("");

        Role role = (Role) editor.getValue();

        assertNull("Should not return a role", role);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsRole() {
        when(roleRepository.findByName(ROLE1_NAME)).thenReturn(role1);
        when(roleRepository.findByName(ROLE2_NAME)).thenReturn(role2);

        editor.setAsText(ROLE1_NAME);

        assertEquals(role1, editor.getValue());
        editor.setAsText(ROLE2_NAME);

        assertEquals(role2, editor.getValue());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsRolesWithWhitespace() {
        when(roleRepository.findByName(ROLE1_NAME)).thenReturn(role1);
        when(roleRepository.findByName(ROLE2_NAME)).thenReturn(role2);

        editor.setAsText(ROLE1_NAME + " ");
        assertEquals(role1, editor.getValue());
        editor.setAsText("   " + ROLE2_NAME);

        assertEquals(role2, editor.getValue());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        MockitoAnnotations.initMocks(this);
        editor = new RoleEditor(roleRepository);
        role1 = new Role();
        role1.setId(11L);
        role1.setName(ROLE1_NAME);

        role2 = new Role();
        role2.setId(12L);
        role2.setName(ROLE2_NAME);
    }

    /**
     * If
     */
    @Test(expected = IllegalArgumentException.class)
    public void roleNoLongerExists() {
        when(roleRepository.findByName(ROLE1_NAME)).thenReturn(null);

        editor.setAsText(ROLE1_NAME);
    }
}
