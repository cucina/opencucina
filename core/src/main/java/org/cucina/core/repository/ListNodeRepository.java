package org.cucina.core.repository;

import java.util.Collection;

import org.cucina.core.model.ListNode;
import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ListNodeRepository
    extends Repository<ListNode, Long> {
    /**
    * JAVADOC Method Level Comments
    *
    * @param listNode JAVADOC.
    * @return true if listNode exists
    */
    boolean exists(ListNode listNode);

    /**
     * Loads <code>ListNode</code>s for the provided type.
     * @param type
     * @return
     */
    Collection<ListNode> findByType(String type);

    /**
     * Load <code>ListNode</code> by id.
     * @param type
     * @param code
     * @return
     */
    ListNode find(Long id);

    /**
     * Save the nodes to the data store.
     * @param nodes
     * @return
     */
    Collection<ListNode> save(Collection<ListNode> nodes);

    /**
     * Save the node to the data store.
     * @param node
     */
    void save(ListNode node);
}
