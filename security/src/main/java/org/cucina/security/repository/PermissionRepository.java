package org.cucina.security.repository;

import java.util.Collection;

import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;

import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface PermissionRepository
    extends Repository<Permission, Long> {
    /**
     * Returns true if there is another <code>Permission</code> with the same
     * <code>Role</code> and <code>Dimension</code>s combination.
     *
     * @param permission
     * @return
     */
    boolean exists(Permission permission);

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    Permission find(Long id);

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    Collection<Permission> findAll();

    /**
     * JAVADOC Method Level Comments
     *
     * @param role
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<Permission> findByRole(Role role);

    /**
     * JAVADOC Method Level Comments
     *
     * @param roleName
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<Permission> findByRoleName(String roleName);

    /**
     * Obtains collection of permissions containing privilege for the user.
     *
     *
     * @param user
     *            JAVADOC.
     * @param privilege
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<Permission> findByUserAndPrivilege(String userName, String privilegeName);
}
