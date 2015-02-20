
package org.cucina.cluster;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface HeartbeatService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     */
    void start(String eventName, String nodeId);

    /**
     * JAVADOC Method Level Comments
     *
     * @param eventName
     *            JAVADOC.
     */
    void stop(String eventName);

    /**
     * JAVADOC Method Level Comments
     */
    void stopAll();
}
