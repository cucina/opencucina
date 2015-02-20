
package org.cucina.core.service;

import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface BeanPopulator {
    /**
     * JAVADOC Method Level Comments
     *
     * @param <T> JAVADOC.
     * @param entity JAVADOC.
     * @param params JAVADOC.
     *
     * @return JAVADOC.
     */
    <T> T populate(T entity, Map<String, Object> params);
}
