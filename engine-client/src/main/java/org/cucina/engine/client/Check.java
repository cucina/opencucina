package org.cucina.engine.client;

import java.util.Map;


/**
 * 
 *
 * @author vlevine
  */
public interface Check {
    /**
     *
     *
     * @param domain .
     * @param parameters .
     *
     * @return .
     */
    boolean test(Object domain, Map<String, Object> parameters);
}
