package org.cucina.sample.engine.client.app;

import java.util.Collection;
import java.util.Map;


/**
 * 
 *
 * @author vlevine
  */
public interface ClientService {
    /**
     *
     *
     * @return .
     */
    Item create();

    /**
     *
     *
     * @param processName .
     *
     * @return .
     */
    Collection<Map<String, String>> loadTransitionInfo(String processName);
}
