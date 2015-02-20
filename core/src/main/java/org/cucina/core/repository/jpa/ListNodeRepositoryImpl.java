package org.cucina.core.repository.jpa;

import java.util.Collection;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.cucina.core.model.ListNode;
import org.cucina.core.model.Message;
import org.cucina.core.repository.ListNodeRepository;
import org.cucina.core.service.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ListNodeRepositoryImpl
    implements ListNodeRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ListNodeRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;
    private I18nService i18nService;

    /**
     * Creates a new ListNodeRepositoryImpl object.
     *
     * @param i18nService JAVADOC.
     */
    public ListNodeRepositoryImpl(I18nService i18nService) {
        Assert.notNull(i18nService, "i18nService is null");
        this.i18nService = i18nService;
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
     * @param listNode JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean exists(ListNode listNode) {
        Locale locale = i18nService.getLocale();
        Message ref = listNode.getLabel();
        String refText = ref.getBestMessage(locale);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Reference text for locale '" + locale + "' is  '" + refText + "'");
        }

        if (refText == null) {
            return false;
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ListNode> cq = cb.createQuery(ListNode.class);
        Root<ListNode> token = cq.from(ListNode.class);
        Predicate wi = cb.equal(token.get("type"), listNode.getType());

        Collection<ListNode> results = entityManager.createQuery(cq.where(wi)).getResultList();

        for (ListNode ln : results) {
            Message message = ln.getLabel();

            if (message == null) {
                continue;
            }

            if (refText.equals(message.getBestMessage(locale))) {
                return true;
            }
        }

        return false;
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
    public void save(ListNode node) {
        if (node.isNew()) {
            entityManager.persist(node);
        } else {
            entityManager.merge(node);
        }
    }
}
