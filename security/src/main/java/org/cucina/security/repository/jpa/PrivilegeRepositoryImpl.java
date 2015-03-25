package org.cucina.security.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.cucina.security.model.Privilege;
import org.cucina.security.repository.PrivilegeRepository;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Repository
public class PrivilegeRepositoryImpl
    implements PrivilegeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilege
     *            JAVADOC.
     */
    @Override
    @Transactional
    public void delete(Privilege privilege) {
        entityManager.remove(privilege);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean exists(String name) {
        Assert.notNull(name, "privilege name cannot be null");

        return null != findByName(name);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Privilege> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Privilege> cq = cb.createQuery(Privilege.class);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional(readOnly = true)
    public Privilege findByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Privilege> cq = cb.createQuery(Privilege.class);
        Root<Privilege> root = cq.from(Privilege.class);
        Predicate wi = cb.equal(root.get("name"), name);

        return entityManager.createQuery(cq.where(wi)).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param name
     *            JAVADOC.
     * @param forUpdate
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Privilege findByNameForUpdate(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Privilege> cq = cb.createQuery(Privilege.class);
        Root<Privilege> root = cq.from(Privilege.class);
        Predicate wi = cb.equal(root.get("name"), name);

        return entityManager.createQuery(cq.where(wi)).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilege
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    @Transactional
    public Privilege save(Privilege privilege) {
        if (privilege.isNew()) {
            entityManager.persist(privilege);
        } else {
            entityManager.merge(privilege);
        }

        return privilege;
    }
}
