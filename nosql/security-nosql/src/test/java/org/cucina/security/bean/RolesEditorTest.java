package org.cucina.security.bean;

import java.math.BigInteger;

import java.util.Collection;

import org.springframework.dao.OptimisticLockingFailureException;

import org.cucina.security.model.Role;
import org.cucina.security.repository.RoleRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RolesEditorTest {
    private static final String ROLE1_NAME = "role1";
    private static final String ROLE2_NAME = "role2";
    private Role role1;
    private Role role2;
    @Mock
    private RoleRepository roleRepository;
    private RolesEditor editor;

    /**
     * If empty text is provided does nothing
     */
    @Test
    public void emptyText() {
        editor.setAsText("");

        @SuppressWarnings("unchecked")
        Collection<Role> roles = (Collection<Role>) editor.getValue();

        assertNotNull("Should return collection of roles", roles);
        assertEquals("Incorrect number roles", 0, roles.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsRoles() {
        when(roleRepository.findByName(ROLE1_NAME)).thenReturn(role1);
        when(roleRepository.findByName(ROLE2_NAME)).thenReturn(role2);

        editor.setAsText(ROLE1_NAME + "," + ROLE2_NAME);

        @SuppressWarnings("unchecked")
        Collection<Role> roles = (Collection<Role>) editor.getValue();

        assertNotNull("Should return collection of roles", roles);
        assertEquals("Incorrect number roles", 2, roles.size());
        assertTrue("Should contain role1", roles.contains(role1));
        assertTrue("Should contain role2", roles.contains(role2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsRolesWithWhitespace() {
        when(roleRepository.findByName(ROLE1_NAME)).thenReturn(role1);
        when(roleRepository.findByName(ROLE2_NAME)).thenReturn(role2);

        editor.setAsText(ROLE1_NAME + " , " + ROLE2_NAME);

        @SuppressWarnings("unchecked")
        Collection<Role> roles = (Collection<Role>) editor.getValue();

        assertNotNull("Should return collection of roles", roles);
        assertEquals("Incorrect number roles", 2, roles.size());
        assertTrue("Should contain role1", roles.contains(role1));
        assertTrue("Should contain role2", roles.contains(role2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        MockitoAnnotations.initMocks(this);
        editor = new RolesEditor(roleRepository);
        role1 = new Role();
        role1.setId(BigInteger.valueOf(11L));
        role1.setName(ROLE1_NAME);

        role2 = new Role();
        role2.setId(BigInteger.valueOf(12L));
        role2.setName(ROLE2_NAME);
    }

    /**
     * If
     */
    @Test(expected = OptimisticLockingFailureException.class)
    public void roleNoLongerExists() {
        when(roleRepository.findByName(ROLE1_NAME)).thenReturn(null);

        editor.setAsText(ROLE1_NAME + "," + ROLE2_NAME);
    }
}
