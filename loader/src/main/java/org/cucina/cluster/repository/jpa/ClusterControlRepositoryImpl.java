package org.cucina.cluster.repository.jpa;

import org.cucina.cluster.model.ClusterControl;
import org.cucina.cluster.repository.ClusterControlRepository;
import org.cucina.core.InstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Date;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClusterControlRepositoryImpl
		implements ClusterControlRepository {
	private static final Logger LOG = LoggerFactory.getLogger(ClusterControlRepositoryImpl.class);
	@PersistenceContext
	private EntityManager entityManager;
	private InstanceFactory instanceFactory;

	/**
	 * Creates a new ClusterControlDaoImpl object.
	 *
	 * @param instanceFactory    JAVADOC.
	 * @param persistenceService JAVADOC.
	 * @param searchDao          JAVADOC.
	 */
	public ClusterControlRepositoryImpl(InstanceFactory instanceFactory) {
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
	 * @param eventName JAVADOC.
	 * @param nodeId    JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	@Transactional
	public boolean complete(String eventName, String nodeId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Cleaning for event '" + eventName + "' and node '" + nodeId + "'");
		}

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaUpdate<ClusterControl> cq = cb.createCriteriaUpdate(ClusterControl.class);
		Root<ClusterControl> root = cq.from(ClusterControl.class);
		Predicate ep = cb.equal(root.get("event"), eventName);
		Path<Object> pc = root.get("complete");
		Predicate cp = cb.equal(pc, false);
		Predicate np = cb.equal(root.get("activeNodeId"), nodeId);

		cq.set(pc, true);

		int rows = entityManager.createQuery(cq.where(cb.and(ep, cp, np))).executeUpdate();

		return rows > 0;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param clusterControl JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean create(String eventName, String nodeId, Map<Object, Object> properties) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Attempting to create clusterControl record for eventName: " + eventName +
					", and nodeId:" + nodeId);
		}

		ClusterControl clusterControl = instanceFactory.getBean(ClusterControl.class.getSimpleName());

		clusterControl.setEvent(eventName);
		clusterControl.setActiveNodeId(nodeId);
		clusterControl.setTimestamp(new Date());
		clusterControl.setComplete(false);
		clusterControl.setProperties(properties);

		try {
			entityManager.persist(clusterControl);

			return true;
		} catch (Exception e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Must have been create elsewhere", e);
			}

			return false;
		}
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean deleteByEventName(String eventName) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<ClusterControl> cq = cb.createCriteriaDelete(ClusterControl.class);
		Root<ClusterControl> root = cq.from(ClusterControl.class);
		Predicate ep = cb.equal(root.get("event"), eventName);
		Predicate cp = cb.equal(root.get("complete"), false);

		int rows = entityManager.createQuery(cq.where(cb.and(ep, cp))).executeUpdate();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Deleted " + rows + " for event " + eventName);
		}

		return rows > 0;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public ClusterControl find(Long id) {
		return entityManager.find(ClusterControl.class, id);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public ClusterControl findByEventNameAndCurrent(String eventName) {
		//select cc from ClusterControl cc where cc.event=?1 and cc.complete=false
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ClusterControl> cq = cb.createQuery(ClusterControl.class);
		Root<ClusterControl> root = cq.from(ClusterControl.class);
		Predicate ep = cb.equal(root.get("event"), eventName);
		Predicate cp = cb.equal(root.get("complete"), false);

		ClusterControl cc = entityManager.createQuery(cq.where(cb.and(ep, cp))).getSingleResult();

		if (LOG.isDebugEnabled()) {
			if (null == cc) {
				LOG.debug("No current record found for event " + eventName);
			} else {
				LOG.debug("Found current for event " + eventName);
			}
		}

		return cc;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 * @param nodeId    JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public ClusterControl findByEventNameAndNodeIdAndCurrent(String eventName, String nodeId) {
		// select cc from ClusterControl cc where cc.event=?1 and cc.complete=false and cc.activeNodeId=?2
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ClusterControl> cq = cb.createQuery(ClusterControl.class);
		Root<ClusterControl> root = cq.from(ClusterControl.class);
		Predicate ep = cb.equal(root.get("event"), eventName);
		Predicate cp = cb.equal(root.get("complete"), false);
		Predicate np = cb.equal(root.get("activeNodeId"), nodeId);

		cq = cq.where(cb.and(ep, cp, np));

		ClusterControl cc = entityManager.createQuery(cq).getSingleResult();

		if (LOG.isDebugEnabled()) {
			if (null == cc) {
				LOG.debug("No current record found for event " + eventName + " and node " + nodeId);
			} else {
				LOG.debug("Found current for event " + eventName + " and node " + nodeId);
			}
		}

		return cc;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param thisNode JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Collection<ClusterControl> findCurrentByNotThisNode(String thisNode) {
		//select cc from ClusterControl cc where cc.complete=false and cc.activeNodeId<>?1
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ClusterControl> cq = cb.createQuery(ClusterControl.class);
		Root<ClusterControl> root = cq.from(ClusterControl.class);
		Predicate cp = cb.equal(root.get("complete"), false);
		Predicate np = cb.notEqual(root.get("activeNodeId"), thisNode);

		Collection<ClusterControl> ccs = entityManager.createQuery(cq.where(cb.and(cp, np)))
				.getResultList();

		if (LOG.isDebugEnabled()) {
			if (ccs.isEmpty()) {
				LOG.debug("Found no incomplete processes on other nodes");
			} else {
				LOG.debug("Found " + ccs.size() + " processes from other nodes");
			}

			for (ClusterControl clusterControl : ccs) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("ClusterControl " + clusterControl);
				}
			}
		}

		return ccs;
	}
}
