package org.cucina.security.repository;

import java.math.BigInteger;
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
    extends Repository<Privilege, BigInteger> {
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
     * Load all Privileges in system.
     *
     * @return Collection<Privilege>.
     */
    Collection<Privilege> findAll();

    /**
     * Load Privilege by name.
     *
     * @param name
     * @return
     */
    Privilege findByName(String name);
}
