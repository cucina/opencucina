package org.cucina.security.repository;

import java.util.Collection;

import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.springframework.data.repository.Repository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * Various convenience methods to load Users
 *
 * @author $Author: $
 * @version $Revision: $
 * @param <T>
 */
public interface UserRepository
    extends UserDetailsService, Repository<User, Long> {
    /**
     * Load Users from the db
     *
     * @return Collection<T>.
     */
    public Collection<User> findAll();

    /**
     * JAVADOC Method Level Comments
     *
     * @param role
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public Collection<User> findByPermission(Permission permission);

    /**
     * JAVADOC Method Level Comments
     *
     * @param role
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    public  Collection<User> findByRole(Role role);

    /**
     * Load standard <code>User</code>s ie those which have system=false
     *
     * @return Collection<User>.
     */
    public Collection<User> findBySystem(boolean system);

    /**
     * JAVADOC Method Level Comments
     *
     * @param user JAVADOC.
     */
    void delete(User user);

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    User find(Long id);

    /**
     * Loads the User
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    UserDetails findByUsernameForUpdate(String username)
        throws UsernameNotFoundException;

    /**
     * JAVADOC Method Level Comments
     *
     * @param user
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    User save(User user);
}
