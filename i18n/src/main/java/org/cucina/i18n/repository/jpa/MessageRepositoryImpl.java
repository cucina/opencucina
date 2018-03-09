package org.cucina.i18n.repository.jpa;

import org.cucina.core.InstanceFactory;
import org.cucina.i18n.model.Message;
import org.cucina.i18n.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Collection;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Repository(value = MessageRepository.MESSAGE_REPOSITORY_ID)
public class MessageRepositoryImpl
		implements MessageRepository {
	private static final Logger LOG = LoggerFactory.getLogger(MessageRepositoryImpl.class);
	@PersistenceContext
	private EntityManager entityManager;
	private InstanceFactory instanceFactory;

	/**
	 * Creates a new MessageRepositoryImpl object.
	 *
	 * @param instanceFactory JAVADOC.
	 */
	@Autowired
	public MessageRepositoryImpl(InstanceFactory instanceFactory) {
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
	 * @return JAVADOC.
	 */
	@Override
	public Collection<Message> findAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);

		cq.from(Message.class);

		return entityManager.createQuery(cq).getResultList();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param pageable JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Collection<Message> findAll(Pageable pageable) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);

		cq.from(Message.class);

		TypedQuery<Message> tq = entityManager.createQuery(cq);

		tq.setFirstResult(pageable.getPageNumber());
		tq.setMaxResults(pageable.getPageSize());

		return tq.getResultList();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param basename JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Collection<Message> findByBasename(String basename) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> root = cq.from(Message.class);
		Path<Object> pb = root.get("baseName");
		Predicate preb;

		if (basename == null) {
			preb = cb.isNull(pb);
		} else {
			preb = cb.equal(pb, basename);
		}

		return entityManager.createQuery(cq.where(preb)).getResultList();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param basename JAVADOC.
	 * @param code     JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Message findByBasenameAndCode(String basename, String code) {
		entityManager.setFlushMode(FlushModeType.COMMIT);

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Message> cq = cb.createQuery(Message.class);
		Root<Message> root = cq.from(Message.class);
		Path<Object> pb = root.get("baseName");
		Predicate preb;

		if (basename == null) {
			preb = cb.isNull(pb);
		} else {
			preb = cb.equal(pb, basename);
		}

		Predicate prec = cb.equal(root.get("messageCd"), code);

		try {
			return entityManager.createQuery(cq.where(cb.and(preb, prec))).getSingleResult();
		} catch (NoResultException e) {
			LOG.info("No record found for basename='" + basename + "' and code='" + code + "'");

			return null;
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param basenames JAVADOC.
	 * @param code      JAVADOC.
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
	 * @param id JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Message findById(Long id) {
		return entityManager.find(Message.class, id);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param basename JAVADOC.
	 * @param locale   JAVADOC.
	 * @param code     JAVADOC.
	 * @param msg      JAVADOC.
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
	 * @return JAVADOC.
	 */
	@Override
	public Message save(Message message) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Saving " + message);
		}

		if (message.isNew()) {
			entityManager.persist(message);
		} else {
			entityManager.merge(message);
		}

		return message;
	}
}
