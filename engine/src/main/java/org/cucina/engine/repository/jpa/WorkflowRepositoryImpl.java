package org.cucina.engine.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.cucina.engine.model.Workflow;
import org.cucina.engine.model.WorkflowToken;
import org.cucina.engine.repository.WorkflowRepository;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class WorkflowRepositoryImpl
    implements WorkflowRepository {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isActive(String definitionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<WorkflowToken> token = cq.from(WorkflowToken.class);

        cq.where(cb.equal(token.get("workflowDefinitionId"), definitionId));
        cq.select(cb.count(token));

        Long result = entityManager.createQuery(cq).getSingleResult();

        return result > 0;
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
     * @param definitionId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean exists(String definitionId) {
        return null != loadByWorkflowId(definitionId);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<String> listAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Workflow> token = cq.from(Workflow.class);

        cq.select(token.<String>get("workflowId"));

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<Workflow> loadAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Workflow> cq = cb.createQuery(Workflow.class);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param definitionId JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Workflow loadByWorkflowId(String definitionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Workflow> cq = cb.createQuery(Workflow.class);
        Root<Workflow> token = cq.from(Workflow.class);

        cq.where(cb.equal(token.get("workflowId"), definitionId));

        try {
            return entityManager.createQuery(cq).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param workflow JAVADOC.
     */
    @Override
    public void save(Workflow workflow) {
        if (workflow.isNew()) {
            entityManager.persist(workflow);
        } else {
            entityManager.merge(workflow);
        }
    }
}
