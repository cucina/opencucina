package org.cucina.security.service;

import org.cucina.security.bean.InstanceFactory;
import org.cucina.security.crypto.PasswordDecryptor;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.repository.RoleRepository;
import org.cucina.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashSet;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class UserPasswordSetterImpl
		implements UserPasswordSetter {
	private static final Logger LOG = LoggerFactory.getLogger(UserPasswordSetterImpl.class);
	@Autowired
	private InstanceFactory instanceFactory;
	@Autowired
	private PasswordDecryptor passwordDecryptor;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param username JAVADOC.
	 * @param password JAVADOC.
	 */
	@Transactional
	@Override
	public void setUserPassword(String username, String password) {
		User user = userRepository.findByUsername(username);

		user.setPassword(password);

		userRepository.save(user);
	}

	/**
	 * Either updates an existing admin user with given password or creates new
	 * one. Disables other administrators, makes certain that ADMINISTRATOR
	 * permissions are assigned only to this user.
	 *
	 * @param username JAVADOC.
	 * @param password JAVADOC.
	 */
	@Override
	@Transactional
	public void createAdminUser(String username, String password) {
		Collection<User> users = userRepository.findBySystem(true);
		User system = null;

		for (User user : users) {
			if (username.equals(user.getUsername())) {
				system = user;
			} else {
				user.setEnabled(false);
				userRepository.save(user);
			}
		}

		if (system == null) {
			system = instanceFactory.getBean(User.class.getName());
		}

		system.setSystem(true);
		system.setAccountNonExpired(true);
		system.setAccountNonLocked(true);
		system.setEnabled(true);
		system.setCredentialsNonExpired(true);
		system.setName(username);
		system.setUsername(username);
		system.setPassword(passwordDecryptor.decrypt(password));

		Collection<Permission> permissions = system.getPermissions();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Existing permissions:" + permissions);
		}

		boolean admin = false;
		Role adminRole = roleRepository.findByName(Role.ADMINISTRATOR);

		Assert.notNull(adminRole, "adminRole is null");

		for (Permission permission : permissions) {
			if (permission.getRole().equals(adminRole)) {
				admin = true;
			}
		}

		if (!admin) {
			LOG.debug("Creating new permission");

			Permission perm = instanceFactory.getBean(Permission.class.getName());

			perm.setName(Role.ADMINISTRATOR);
			perm.setRole(adminRole);

			Collection<Permission> adPerms = new HashSet<Permission>();

			adPerms.add(perm);
			system.setPermissions(adPerms);
		}

		userRepository.save(system);

		// TODO update savedsearches with admin user as owner. only for
		// specified savedsearches.
	}
}
