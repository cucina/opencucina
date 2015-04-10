package org.cucina.sample.engine.client.app;

import org.springframework.data.repository.CrudRepository;


/**
 *
 *
 * @author vlevine
 */
public interface ItemRepository
    extends CrudRepository<Item, Long> {
}
