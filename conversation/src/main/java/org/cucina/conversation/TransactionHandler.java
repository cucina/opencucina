package org.cucina.conversation;

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
