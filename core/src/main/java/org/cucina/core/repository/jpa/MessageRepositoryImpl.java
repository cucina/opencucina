package org.cucina.core.repository.jpa;

import java.util.Collection;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.LocaleUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.model.Message;
import org.cucina.core.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MessageRepositoryImpl
    implements MessageRepository {
    private static final Logger LOG = LoggerFactory.getLogger(MessageRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    private InstanceFactory instanceFactory;
    private Locale defaultLocale;

    /**
     * Creates a new MessageRepositoryImpl object.
     *
     * @param instanceFactory JAVADOC.
     */
    public MessageRepositoryImpl(InstanceFactory instanceFactory) {
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Locale getDefaultLocale() {
        if (defaultLocale == null) {
            return Locale.getDefault();
        }

        return defaultLocale;
    }

    /**
     * Set default Locale. Validates locale String is valid before setting
     * locale
     *
     * @param locale
     */
    public void setDefaultLocaleString(String locale) {
        try {
            defaultLocale = LocaleUtils.toLocale(locale);
        } catch (IllegalArgumentException e) {
            LOG.warn("Invalid locale has been set up [" + locale + "]");
            throw e;
        }
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
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message find(Long id) {
        return entityManager.find(Message.class, id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param basename JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Message> findByBasename(String basename) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> root = cq.from(Message.class);
        Predicate preb = cb.equal(root.get("baseName"), basename);

        return entityManager.createQuery(cq.where(preb)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param basename JAVADOC.
     * @param code JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message findByBasenameAndCode(String basename, String code) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> root = cq.from(Message.class);
        Predicate preb = cb.equal(root.get("baseName"), basename);
        Predicate prec = cb.equal(root.get("messageCd"), code);

        return entityManager.createQuery(cq.where(cb.and(preb, prec))).getSingleResult();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param basenames JAVADOC.
     * @param code JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Message> findByBasenamesAndCode(Collection<String> basenames, String code) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> root = cq.from(Message.class);
        Predicate preb = root.get("baseName").in(basenames);
        Predicate prec = cb.equal(root.get("messageCd"), code);

        return entityManager.createQuery(cq.where(cb.and(preb, prec))).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param code JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Message> findByCode(String code) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> root = cq.from(Message.class);
        Predicate prec = cb.equal(root.get("messageCd"), code);

        return entityManager.createQuery(cq.where(prec)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param basename JAVADOC.
     * @param locale JAVADOC.
     * @param code JAVADOC.
     * @param msg JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message save(String basename, String locale, String code, String msg) {
        Message message;

        try {
            message = findByBasenameAndCode(basename, code);
        } catch (NoResultException e) {
            message = null;
        }

        if (message == null) {
            message = instanceFactory.getBean(Message.class.getSimpleName());
            message.setBaseName(basename);
            message.setMessageCd(code);
        }

        message.setMessageTx(msg, locale);

        return save(message);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param messages JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Message> save(Collection<Message> messages) {
        for (Message message : messages) {
            save(message);
        }

        return messages;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message save(Message message) {
        if (message.isNew()) {
            entityManager.persist(message);
        } else {
            entityManager.merge(message);
        }

        return message;
    }
}
