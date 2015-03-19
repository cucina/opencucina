package org.cucina.i18n.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import org.cucina.i18n.model.ListNode;
import org.cucina.i18n.repository.ListNodeRepository;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Repository(value = ListNodeRepository.LISTNODE_REPOSITORY_ID)
public class ListNodeRepositoryImpl
    implements ListNodeRepository {
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
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public ListNode find(Long id) {
        return entityManager.find(ListNode.class, id);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<ListNode> findByType(String type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ListNode> cq = cb.createQuery(ListNode.class);
        Root<ListNode> token = cq.from(ListNode.class);
        Predicate wi = cb.equal(token.get("type"), type);

        return entityManager.createQuery(cq.where(wi)).getResultList();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param nodes JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Collection<ListNode> save(Collection<ListNode> nodes) {
        for (ListNode listNode : nodes) {
            save(listNode);
        }

        return nodes;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param node JAVADOC.
     */
    @Override
    @Transactional
    public Long save(ListNode node) {
        if (node.isNew()) {
            entityManager.persist(node);
        } else {
            entityManager.merge(node);
        }

        return node.getId();
    }
}
