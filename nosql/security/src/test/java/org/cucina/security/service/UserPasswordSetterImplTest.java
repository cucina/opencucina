package org.cucina.security.service;

import org.cucina.security.bean.InstanceFactory;
import org.cucina.security.crypto.PasswordDecryptor;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.repository.RoleRepository;
import org.cucina.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
		when(userRepository.findByUsername(USERNAME)).thenReturn(user);
		impl.setUserPassword(USERNAME, PASSWORD);

		assertEquals("Should have set password", PASSWORD, user.getPassword().getValue());
		verify(userRepository).findByUsername(USERNAME);
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

		when(instanceFactory.getBean(User.class.getName())).thenReturn(sysUser);
		when(passwordDecryptor.decrypt(ADMIN_USER)).thenReturn(ADMIN_USER);
		when(roleRepository.findByName(Role.ADMINISTRATOR)).thenReturn(adminRole);
		when(instanceFactory.getBean(Permission.class.getName())).thenReturn(permission);
		impl.createAdminUser(ADMIN_USER, ADMIN_USER);

		assertEquals(true, sysUser.isSystem());
		assertEquals(true, sysUser.isAccountNonExpired());
		assertEquals(true, sysUser.isAccountNonLocked());
		assertEquals(true, sysUser.isEnabled());
		assertEquals(true, sysUser.isCredentialsNonExpired());
		assertEquals(ADMIN_USER, sysUser.getName());
		assertEquals(ADMIN_USER, sysUser.getUsername());
		assertEquals(ADMIN_USER, sysUser.getPassword().getValue());
		assertEquals(1, sysUser.getPermissions().size());
		assertEquals(permission, sysUser.getPermissions().iterator().next());
		verify(userRepository).findBySystem(true);
		verify(instanceFactory).getBean(User.class.getName());
		verify(passwordDecryptor).decrypt(ADMIN_USER);
		verify(instanceFactory).getBean(Permission.class.getName());
	}

	/**
	 * Sets up for test
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		impl = new UserPasswordSetterImpl();
		ReflectionTestUtils.setField(impl, "instanceFactory", instanceFactory);
		ReflectionTestUtils.setField(impl, "passwordDecryptor", passwordDecryptor);
		ReflectionTestUtils.setField(impl, "roleRepository", roleRepository);
		ReflectionTestUtils.setField(impl, "userRepository", userRepository);
		user = new User();
	}
}
