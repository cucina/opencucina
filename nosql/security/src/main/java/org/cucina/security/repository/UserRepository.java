package org.cucina.security.repository;

import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.springframework.data.repository.Repository;

import java.util.Collection;


/**
 * Data storage interface.
 *
 * @author vlevine
 */
public interface UserRepository
		extends Repository<User, Long> {
	void delete(User user);

	Collection<User> findAll();

	Collection<User> findByPermissions(Permission permission);

	Collection<User> findByPermissionsRole(Role role);

	Collection<User> findByPermissionsRolePrivilegesName(String privilegeName);

	Collection<User> findBySystem(boolean system);

	User findByUsername(String username);

	Long removeByUsername(String username);

	User save(User user);
}
