package org.cucina.security.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cucina.security.model.Role;
import org.cucina.security.repository.RoleRepository;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RoleRepositoryImpl
    implements RoleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JAVADOC Method Level Comments
     *
     * @param entityManager JAVADOC.
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @param role JAVADOC.
    */
    @Override
    public void delete(Role role) {
        entityManager.remove(role);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean exists(String name) {
        Assert.notNull(name, "role name cannot be null");

        return null != findByName(name);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Role> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Role findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> cq = cb.createQuery(Role.class);
        Root<Role> root = cq.from(Role.class);
        Predicate wi = cb.equal(root.get("name"), name);

        return entityManager.createQuery(cq.where(wi)).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param role JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Role save(Role role) {
        if (role.isNew()) {
            entityManager.persist(role);
        } else {
            entityManager.merge(role);
        }

        return role;
    }
}
