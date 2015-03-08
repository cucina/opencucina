package org.cucina.search.repository;

import java.util.Collection;

import org.cucina.search.model.PersistableSearch;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface SavedSearchRepository {
    /**
     * Delete object of type SavedSearch
     *
     * @param savedSearch SavedSearch.
     */
    void deleteById(Long id);

    /**
     * Load SavedSearch by id.
     * @param type SavedSearch.
     */
    PersistableSearch findById(Long id);

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     * @param owner JAVADOC.
     *
     * @return JAVADOC.
     */
    PersistableSearch findByNameAndOwner(String name, UserDetails owner);

    /**
     * JAVADOC Method Level Comments
     *
     * @param user JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<PersistableSearch> findByUser(UserDetails user);

    /**
     * JAVADOC Method Level Comments
     *
     * @param user JAVADOC.
     * @param returnPublic JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<PersistableSearch> findByUserAndPublic(UserDetails user, boolean returnPublic);

    /**
     * Persist object of type SavedSearch.
     *
     * @param savedSearch SavedSearch.
     */
    void save(PersistableSearch savedSearch);
}
