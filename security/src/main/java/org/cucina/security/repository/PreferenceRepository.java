package org.cucina.security.repository;

import java.util.Collection;

import org.cucina.security.model.Preference;
import org.springframework.data.repository.Repository;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface PreferenceRepository
    extends Repository<Preference, Long> {
    /**
        * Delete the preference with the supplied key.
        * @param key
        */
    void delete(String key);

    /**
     * Returns the <code>Preference</code> for the supplied
     * preference name for the current <code>User</code>
     * @param name Preference name.
     *
     * @return Preference.
     */
    Preference load(String name);

    /**
     * Returns the <code>Preference</code> for the supplied
     * preference name and user.
     * @param user UserDetails.
     * @param name preference name.
     *
     * @return Preference
     */
    Preference load(UserDetails user, String name);

    /**
    * JAVADOC Method Level Comments
    *
    * @return JAVADOC.
    */
    Collection<Preference> loadAll();

    /**
     * JAVADOC Method Level Comments
     *
     * @param preference JAVADOC.
     */
    void save(Preference preference);

    /**
     * JAVADOC Method Level Comments
     *
     * @param preferences JAVADOC.
     */
    void saveAll(Collection<Preference> preferences);
}
