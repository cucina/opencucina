package org.cucina.security.repository;

import java.util.Collection;

import org.cucina.security.model.Privilege;
import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface PrivilegeRepository
    extends Repository<Privilege, Long> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param privilege JAVADOC.
     *
     * @return JAVADOC.
     */
    Privilege save(Privilege privilege);

    /**
     * JAVADOC Method Level Comments
     *
     * @param privilege JAVADOC.
     */
    void delete(Privilege privilege);

    /**
     * Returns true if this privilege name exists
     *
     * @param name String.
     *
     * @return boolean.
     */
    boolean exists(String name);

    /**
     * Load all Privileges in system.
     *
     * @return Collection<Privilege>.
     */
    Collection<Privilege> findAll();

    /**
     * Load Privilege by name, it cannot be assumed that the returned Privilege has been
     * loaded for update so use the loadPrivilege(String name, boolean forUpdate) instead.
     *
     * @param name
     * @return
     */
    Privilege findByName(String name);

    /**
     * Allows specifying forUpdate true which will ensure the <code>Privilege</code> has been
     * loaded in a editable state.
     * @param name
     * @param forUpdate
     * @return
     */
    Privilege findByNameForUpdate(String name);
}
