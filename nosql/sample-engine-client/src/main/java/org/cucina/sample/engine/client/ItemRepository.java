package org.cucina.sample.engine.client;

import java.math.BigInteger;

import org.springframework.data.repository.CrudRepository;


/**
 *
 *
 * @author vlevine
 */
public interface ItemRepository
    extends CrudRepository<Item, BigInteger> {
}
