
package org.cucina.loader.agent;

import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface AgentRunner {
    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     * @param properties JAVADOC.
     */
    void run(String eventName, Map<Object, Object> properties);
}
