
package org.cucina.cluster;

import java.io.Serializable;
import java.util.Map;

import org.cucina.cluster.event.SendBroadcastClusterEvent;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public interface ClusterBroadcastService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     * @param properties JAVADOC.
     */
    void broadcast(String eventName, Map<String, Serializable> properties);

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName JAVADOC.
     */
    void broadcast(String eventName);

    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     */
    void handle(SendBroadcastClusterEvent event);
}
