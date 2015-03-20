package org.cucina.engine.repository.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.cucina.core.InstanceFactory;
import org.cucina.engine.repository.DomainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Repository
public class DomainRepositoryImpl
    implements DomainRepository {
    private static final Logger LOG = LoggerFactory.getLogger(DomainRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    private InstanceFactory instanceFactory;

    /**
     * Creates a new DomainRepositoryImpl object.
     *
     * @param instanceFactory JAVADOC.
     */
    public DomainRepositoryImpl(InstanceFactory instanceFactory) {
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
    }

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
     * @param domain JAVADOC.
     */
    @Override
    public void delete(Persistable<Long> domain) {
        entityManager.remove(domain);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Persistable<Long> load(String type, Long id) {
        Class<?> clazz = instanceFactory.getClassType(type);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<?> cq = cb.createQuery(clazz);
        Root<?> token = cq.from(clazz);

        cq.where(cb.equal(token.get("id"), id));

        try {
            return (Persistable<Long>) entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            LOG.info("Oops", e);

            return null;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param domain JAVADOC.
     */
    @Override
    public void save(Persistable<Long> domain) {
        if (domain.isNew()) {
            entityManager.persist(domain);
        } else {
            entityManager.merge(domain);
        }
    }
}
