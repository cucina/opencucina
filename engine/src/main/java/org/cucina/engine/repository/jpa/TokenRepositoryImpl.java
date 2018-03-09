package org.cucina.engine.repository.jpa;

import org.apache.commons.lang3.ArrayUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.core.model.PersistableEntity;
import org.cucina.engine.model.HistoryRecord;
import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TokenRepositoryImpl
		extends WorkflowRepositorySupport
		implements TokenRepository {
	private static final Logger LOG = LoggerFactory.getLogger(TokenRepositoryImpl.class);
	@PersistenceContext
	private EntityManager entityManager;
	private InstanceFactory instanceFactory;

	/**
	 * Creates a new TokenRepositoryImpl object.
	 *
	 * @param entityManager JAVADOC.
	 */
	@Autowired
	public TokenRepositoryImpl(InstanceFactory instanceFactory) {
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
	 * @param token JAVADOC.
	 */
	@Override
	public void delete(ProcessToken token) {
		entityManager.remove(token);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param token JAVADOC.
	 */
	@Override
	public void deleteDeep(ProcessToken token) {
		Object domain = token.getDomainObject();

		entityManager.remove(token);
		entityManager.remove(domain);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param ids             JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	@Transactional
	public Collection<ProcessToken> findByApplicationTypeAndIds(String applicationType,
																Serializable... ids) {
		Assert.notNull(applicationType, "type cannot be null");

		if (ArrayUtils.isEmpty(ids)) {
			LOG.debug("Array ids is empty, returning null");

			return null;
		}

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Tuple> tcq = cb.createTupleQuery();
		Root<ProcessToken> rt = tcq.from(ProcessToken.class);
		Path<Serializable> pid = rt.get("domainObjectId");
		Predicate tokp = cb.and(cb.equal(rt.get("domainObjectType"), applicationType), pid.in(ids));

		Class<? extends PersistableEntity> clazz = resolveClass(applicationType);
		Root<? extends PersistableEntity> rc = tcq.from(clazz);
		Predicate clap = cb.equal(pid, rc.get("id"));

		tcq.multiselect(rt, rc).where(cb.and(clap, tokp));

		Collection<Tuple> results = entityManager.createQuery(tcq).getResultList();

		if ((results == null) || (results.size() == 0)) {
			LOG.warn("Failed to find workflow instances for the objects:" + applicationType + ":" +
					ids);
			throw new IllegalArgumentException("Failed to find workflow instances for the objects:" +
					applicationType + ":" + ids);
		}

		return populate(results);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param domain JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	@Transactional
	public ProcessToken findByDomain(Persistable<?> domain) {
		Assert.notNull(domain, "Domain is null");

		BeanWrapper bw = new BeanWrapperImpl(domain);

		Assert.isTrue(bw.isReadableProperty("id"),
				"No 'id' property on object of type '" + domain.getClass() + "'");

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ProcessToken> cq = cb.createQuery(ProcessToken.class);
		Root<ProcessToken> token = cq.from(ProcessToken.class);

		cq.where(cb.and(cb.equal(token.get("domainObjectType"), domain.getClass().getSimpleName()),
				cb.equal(token.get("domainObjectId"), bw.getPropertyValue("id"))));

		try {
			ProcessToken wt = entityManager.createQuery(cq).getSingleResult();

			wt.setDomainObject(domain);

			return wt;
		} catch (Exception e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Ignoring this", e);
			}

			return null;
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param workflowId      JAVADOC.
	 * @param placeId         JAVADOC.
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	@Transactional
	public Collection<Long> findDomainIdsByWorkflowIdPlaceIdApplicationType(
			final String workflowId, final String placeId, final String applicationType) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ProcessToken> token = cq.from(ProcessToken.class);
		Predicate wi = cb.equal(token.get("workflowDefinitionId"), workflowId);
		Predicate pi = cb.equal(token.get("placeId"), placeId);
		Predicate at = cb.equal(token.get("domainObjectType"), applicationType);
		Predicate and = cb.and(wi, pi, at);

		cq.select(token.<Long>get("domainObjectId"));

		return entityManager.createQuery(cq.where(and)).getResultList();
	}

	@Override
	public List<HistoryRecord> findHistoryRecordsByDomainObjectIdAndDomainObjectType(
			Serializable id, String applicationType) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<HistoryRecord> cq = cb.createQuery(HistoryRecord.class);
		Root<ProcessToken> token = cq.from(ProcessToken.class);
		Predicate pi = cb.equal(token.get("domainObjectId"), id);
		Predicate at = cb.equal(token.get("domainObjectType"), applicationType);
		Predicate and = cb.and(pi, at);

		cq.select(token.<HistoryRecord>get("histories"));

		//        cq.orderBy(cb.desc(token.get("histories").get("modifiedDate")));
		return entityManager.createQuery(cq.where(and)).getResultList();
	}

	/**
	 * Creates or updates depending on whether the token is new.
	 */
	@Override
	public void save(ProcessToken token) {
		if (token.isNew()) {
			create(token);
		} else {
			update(token);
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationType JAVADOC.
	 * @return JAVADOC.
	 */
	protected Class<? extends PersistableEntity> resolveClass(String applicationType) {
		return instanceFactory.getClassType(applicationType);
	}

	private void create(ProcessToken token) {
		Assert.notNull(token, "token cannot be null");
		Assert.isNull(token.getId(), "This must be a new token");
		Assert.notNull(token.getDomainObject(), "token must have a domainObject");

		if (token.getDomainObject().getId() == null) {
			Persistable<? extends Serializable> domain = token.getDomainObject();

			entityManager.persist(domain);

			Assert.notNull(domain.getId(), "id must now be set on domainObject");
			token.setDomainObject(domain);
		} else {
			entityManager.merge(token.getDomainObject());
		}

		// Now set id and type of domainObject on Token
		token.setDomainObjectId(token.getDomainObject().getId());

		BeanWrapper beanWrapper = new BeanWrapperImpl(token.getDomainObject());

		token.setDomainObjectType((String) beanWrapper.getPropertyValue("applicationType"));

		entityManager.persist(token);
	}

	private void update(ProcessToken token) {
		Assert.notNull(token, "token cannot be null");
		Assert.notNull(token.getId(), "This must not be a new token");
		Assert.notNull(token.getDomainObject(), "token must have a domainObject");
		Assert.notNull(token.getDomainObject().getId(), "domainObject must have an id");
		Assert.isTrue(token.getDomainObject().getId().equals(token.getDomainObjectId()),
				"domainObject's id must match the reference on token");

		entityManager.merge(token.getDomainObject());
		entityManager.merge(token);
	}
}
