package org.cucina.i18n.repository;

import java.util.Collection;

import org.cucina.i18n.model.ListNode;
import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ListNodeRepository
    extends Repository<ListNode, Long> {
	String LISTNODE_REPOSITORY_ID="listNodeRepository";
  
    /**
     * Load <code>ListNode</code> by id.
     * @param type
     * @param code
     * @return
     */
    ListNode find(Long id);

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Loads <code>ListNode</code>s for the provided type.
     * @param type
     * @return
     */
    Collection<ListNode> findByType(String type);

    /**
     * JAVADOC Method Level Comments
     *
     * @param nodes JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Save the nodes to the data store.
     * @param nodes
     * @return
     */
    Collection<ListNode> save(Collection<ListNode> nodes);

    /**
     * Save the node to the data store.
     * @param node
     * @return 
     */
    Long save(ListNode node);
}
