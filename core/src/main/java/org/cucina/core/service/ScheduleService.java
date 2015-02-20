
package org.cucina.core.service;

import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ScheduleService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     * @param group JAVADOC.
     * @param interval JAVADOC.
     * @param bean JAVADOC.
     * @param method JAVADOC.
     * @param properties JAVADOC.
     */
    void start(String name, String group, long interval, Object bean, String method,
        Map<String, Object> properties);

    /**
     * JAVADOC Method Level Comments
     *
     * @param name JAVADOC.
     * @param group JAVADOC.
     */
    void stop(String name, String group);
}
