
package org.cucina.core.service;

import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ContextService {
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    Map<Object, Object> getAll();

    /**
     * JAVADOC Method Level Comments
     */
    void clear();

    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param key JAVADOC.
     *
     * @return JAVADOC.
     */
    <T> T get(Object key);

    /**
     * JAVADOC Method Level Comments
     *
     * @param key
     *            JAVADOC.
     * @param value
     *            JAVADOC.
     */
    void put(Object key, Object value);

    /**
     * JAVADOC Method Level Comments
     *
     * @param properties
     *            JAVADOC.
     */
    void putAll(Map<Object, Object> properties);
}
