package org.cucina.security.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Repository("userRepository")
public class UserRepositoryImpl
    implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JAVADOC Method Level Comments
     *
     * @param user
     *            JAVADOC.
     */
    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public User find(Long id) {
        return entityManager.find(User.class, id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<User> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param permission
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<User> findByPermission(Permission permission) {
        // select user from User user where ?1 member of user.permissions
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Predicate pi = cb.isMember(permission, root.<Collection<Permission>>get("permissions"));

        return entityManager.createQuery(cq.where(pi)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param role
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<User> findByRole(Role role) {
        // select distinct user from User user where user.permissions.role = ?1
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);

        Predicate in = cb.in(root.<Collection<Permission>>get("permissions").get("role")).value(role);

        return entityManager.createQuery(cq.where(in)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param system
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<User> findBySystem(boolean system) {
        // select user from User user where user.system = false
        // select user from User user where user.system = true and user.active =
        // true and user.enabled = true
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Predicate pred;

        if (system) {
            Predicate sp = cb.equal(root.get("system"), true);
            Predicate ap = cb.equal(root.get("active"), true);
            Predicate ep = cb.equal(root.get("enabled"), true);

            pred = cb.and(sp, ap, ep);
        } else {
            pred = cb.equal(root.get("system"), false);
        }

        return entityManager.createQuery(cq.where(pred)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws UsernameNotFoundException
     *             JAVADOC.
     */
    @Override
    @Transactional
    public UserDetails findByUsernameForUpdate(String username)
        throws UsernameNotFoundException {
        User user = loadByName(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username
     *            JAVADOC.
     *
     * @return JAVADOC.
     *
     * @throws UsernameNotFoundException
     *             JAVADOC.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {
        User user = loadByName(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param user
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public User save(User user) {
        if (user.isNew()) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }

        return user;
    }

    private User loadByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Predicate wi = cb.equal(root.get("username"), name);

        return entityManager.createQuery(cq.where(wi)).getSingleResult();
    }
}
