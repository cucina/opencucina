package org.cucina.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.cucina.core.InstanceFactory;
import org.cucina.security.crypto.PasswordDecryptor;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.repository.PermissionRepository;
import org.cucina.security.repository.RoleRepository;
import org.cucina.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test that UserPasswordSetterImpl functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UserPasswordSetterImplTest {
    private static final String ADMIN_USER = "admin";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pwd";
    @Mock
    private InstanceFactory instanceFactory;
    @Mock
    private PasswordDecryptor passwordDecryptor;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private RoleRepository roleRepository;
    private User user;
    private UserPasswordSetterImpl impl;
    @Mock
    private UserRepository userRepository;

    /**
     * Sets password and updates user correctly.
     */
    @Test
    public void setPassword() {
        when(userRepository.loadUserByUsername(USERNAME)).thenReturn(user);
        impl.setUserPassword(USERNAME, PASSWORD);

        assertEquals("Should have set password", PASSWORD, user.getPassword());
        verify(userRepository).loadUserByUsername(USERNAME);
        verify(userRepository).save(user);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void createAdminUser() {
        when(userRepository.findBySystem(true)).thenReturn(Collections.<User>emptySet());

        User sysUser = new User();
        Role adminRole = new Role();
        Permission permission = new Permission();

        when(instanceFactory.getBean(User.class.getSimpleName())).thenReturn(sysUser);
        when(passwordDecryptor.decrypt(ADMIN_USER)).thenReturn(ADMIN_USER);
        when(roleRepository.findByName(Role.ADMINISTRATOR)).thenReturn(adminRole);
        when(permissionRepository.findByRole(adminRole))
            .thenReturn(Collections.<Permission>emptySet());
        when(instanceFactory.getBean(Permission.TYPE)).thenReturn(permission);
        impl.createAdminUser(ADMIN_USER, ADMIN_USER);

        assertEquals(true, sysUser.isSystem());
        assertEquals(true, sysUser.isAccountNonExpired());
        assertEquals(true, sysUser.isAccountNonLocked());
        assertEquals(true, sysUser.isActive());
        assertEquals(true, sysUser.isEnabled());
        assertEquals(true, sysUser.isCredentialsNonExpired());
        assertEquals(ADMIN_USER, sysUser.getName());
        assertEquals(ADMIN_USER, sysUser.getUsername());
        assertEquals(ADMIN_USER, sysUser.getPassword());
        assertEquals(1, sysUser.getPermissions().size());
        assertEquals(permission, sysUser.getPermissions().iterator().next());
        verify(userRepository).findBySystem(true);
        verify(instanceFactory).getBean(User.class.getSimpleName());
        verify(passwordDecryptor).decrypt(ADMIN_USER);
        verify(permissionRepository).findByRole(adminRole);
        verify(instanceFactory).getBean(Permission.TYPE);
    }

    /**
     * Sets up for test
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        impl = new UserPasswordSetterImpl(userRepository, instanceFactory, passwordDecryptor,
                roleRepository, permissionRepository);
        user = new User();
    }
}
