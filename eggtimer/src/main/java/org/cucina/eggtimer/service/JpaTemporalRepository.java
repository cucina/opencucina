package org.cucina.eggtimer.service;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import javax.transaction.Transactional;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class JpaTemporalRepository
    implements TemporalRepository {
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
    * @param date JAVADOC.
    *
    * @return JAVADOC.
    */
    @Override
    @Transactional
    public boolean beforeCurrentDate(Date date) {
        assure();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TimeAtom> cq = cb.createQuery(TimeAtom.class);

        cq.from(TimeAtom.class);

        try {
            entityManager.createQuery(cq.where(cb.greaterThan(cb.currentDate(), date)))
                         .getSingleResult();

            return true;
        } catch (NoResultException e) {
            return false;
        } catch (NonUniqueResultException ex) {
           return true;
        }
    }

    private void assure() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TimeAtom> cq = cb.createQuery(TimeAtom.class);

        cq.from(TimeAtom.class);

        try {
            entityManager.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            entityManager.persist(new TimeAtom());
        } catch (NonUniqueResultException ex) {
            // never mind
        }
    }
}
