package org.cucina.engine.repository;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.Repository;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface DomainRepository
    extends Repository<Persistable<?extends Serializable>, Serializable> {
    /**
     * JAVADOC Method Level Comments
     *
     * @param domain JAVADOC.
     */
    void delete(Persistable<?extends Serializable> domain);

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    Persistable<?extends Serializable> load(String type, Serializable id);

    /**
     * JAVADOC Method Level Comments
     *
     * @param domain JAVADOC.
     */
    void save(Persistable<?extends Serializable> domain);
}
