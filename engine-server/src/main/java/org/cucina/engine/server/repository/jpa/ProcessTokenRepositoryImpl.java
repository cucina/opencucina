package org.cucina.engine.server.repository.jpa;

import org.cucina.engine.model.ProcessToken;
import org.cucina.engine.repository.jpa.WorkflowRepositorySupport;
import org.cucina.engine.server.model.EntityDescriptor;
import org.cucina.engine.server.repository.ProcessTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Collections;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Repository
public class ProcessTokenRepositoryImpl
		extends WorkflowRepositorySupport
		implements ProcessTokenRepository {
	private static final Logger LOG = LoggerFactory.getLogger(ProcessTokenRepositoryImpl.class);
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
	 * Return workflowDefitinionIds vs number of instances.
	 *
	 * @return JAVADOC.
	 */
	@Override
	public Collection<Object[]> countByGroupProcessDefinitionId() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
		Root<ProcessToken> token = cq.from(ProcessToken.class);
		Path<String> wfid = token.get("processDefinitionId");

		cq.multiselect(wfid, cb.count(wfid));
		cq.groupBy(wfid);

		return entityManager.createQuery(cq).getResultList();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param wfid JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Collection<ProcessToken> findByProcessDefinitionId(String wfid) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> tcq = cb.createTupleQuery();
		Root<ProcessToken> rt = tcq.from(ProcessToken.class);
		Predicate tokp = cb.equal(rt.get("processDefinitionId"), wfid);
		Path<Object> pid = rt.get("domainObjectId");

		Root<EntityDescriptor> rc = tcq.from(EntityDescriptor.class);
		Predicate clap = cb.equal(pid, rc.get("localId"));

		tcq.multiselect(rt, rc).where(cb.and(clap, tokp));

		Collection<Tuple> results = entityManager.createQuery(tcq).getResultList();

		if ((results == null) || (results.size() == 0)) {
			LOG.warn("Failed to find workflow instances for the workflowId:" + wfid);

			return Collections.emptyList();
		}

		return populate(results);
	}
}
