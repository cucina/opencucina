package org.cucina.engine.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface DomainRepository
    extends Repository<Persistable<Long>, Long> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param domain JAVADOC.
     */
    void delete(Persistable<Long> domain);

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    Persistable<Long> load(String type, Long id);

    /**
     * JAVADOC Method Level Comments
     *
     * @param domain JAVADOC.
     */
    void save(Persistable<Long> domain);
}
