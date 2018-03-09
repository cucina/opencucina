package org.cucina.security.repository;

import org.cucina.security.model.Role;
import org.springframework.data.repository.Repository;

import java.util.Collection;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface RoleRepository
		extends Repository<Role, Long> {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param role JAVADOC.
	 */
	void delete(Role role);

	/**
	 * Returns true if this role name exists
	 *
	 * @param name String.
	 * @return boolean.
	 */
	int countByName(String name);

	/**
	 * Load all Roles in system
	 *
	 * @return Collection<Role>.
	 */
	Collection<Role> findAll();

	/**
	 * Load Role by name
	 *
	 * @param name
	 * @return
	 */
	Role findByName(String name);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param role JAVADOC.
	 * @return JAVADOC.
	 */
	Role save(Role role);
}
