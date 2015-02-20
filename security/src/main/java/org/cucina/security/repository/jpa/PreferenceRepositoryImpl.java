package org.cucina.security.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.cucina.security.ContextUserAccessor;
import org.cucina.security.model.Preference;
import org.cucina.security.model.User;
import org.cucina.security.repository.PreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PreferenceRepositoryImpl
    implements PreferenceRepository {
    private static final Logger LOG = LoggerFactory.getLogger(PreferenceRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JAVADOC Method Level Comments
     *
     * @param entityManager
     *            JAVADOC.
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param key
     *            JAVADOC.
     */
    @Override
    public void delete(String key) {
        Preference p = load(key);

        if (p != null) {
            entityManager.remove(p);
        }
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
    public Preference load(String name) {
        return load(getUser(), name);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param user
     *            JAVADOC.
     * @param name
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Preference load(UserDetails user, String name) {
        // select p from Preference p where p.owner=?1 and p.name=?2
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Preference> cq = cb.createQuery(Preference.class);
        Root<Preference> root = cq.from(Preference.class);
        Predicate po = cb.equal(root.get("owner"), user);
        Predicate pn = cb.equal(root.get("name"), name);
        Predicate and = cb.and(po, pn);

        return entityManager.createQuery(cq.where(and)).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Preference> loadAll() {
        // select p from Preference p where p.owner=?1
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Preference> cq = cb.createQuery(Preference.class);
        Root<Preference> root = cq.from(Preference.class);
        Predicate po = cb.equal(root.get("owner"), getUser());

        return entityManager.createQuery(cq.where(po)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param preference
     *            JAVADOC.
     */
    @Override
    public void save(Preference preference) {
        if (StringUtils.isEmpty(preference.getName())) {
            LOG.warn("Cannot save preference with empty name");

            return;
        }

        if (preference.getOwner() == null) {
            preference.setOwner(getUser());
        }

        if (preference.isNew()) {
            entityManager.persist(preference);
        } else {
            entityManager.merge(preference);
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param preferences
     *            JAVADOC.
     */
    @Override
    public void saveAll(Collection<Preference> preferences) {
    	User user = getUser();
        for (Preference preference : preferences) {
            if (preference.getOwner() == null) {
                preference.setOwner(user);
            }

            save(preference);
        }
    }

    private User getUser() {
        Object u = ContextUserAccessor.getCurrentUser();

        if (u instanceof User) {
            return (User) u;
        }

        LOG.warn("Current user is not an instance of User");

        return null;
    }
}
