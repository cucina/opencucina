package org.cucina.engine.client.service;

import java.io.Serializable;


/**
 * 
 *
 * @author vlevine
  */
public interface TransactionHandler {
    /**
     *
     *
     * @param entityType .
     */
    void registerTxHandler(String entityType, Serializable... ids);
}
