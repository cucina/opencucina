package org.cucina.i18n.repository;

import java.util.Collection;

import org.cucina.i18n.model.ListItem;
import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ListItemRepository
    extends Repository<ListItem, Long> {
	String LISTNODE_REPOSITORY_ID="listNodeRepository";
  
    /**
     * Load <code>ListNode</code> by id.
     * @param type
     * @param code
     * @return
     */
    ListItem findById(Long id);

    /**
     * Loads <code>ListNode</code>s for the provided type.
     * @param type
     * @return
     */
    Collection<ListItem> findByType(String type);

    /**
     * Save the node to the data store.
     * @param node
     * @return 
     */
    ListItem save(ListItem node);
}
