
package org.cucina.cluster.event;


/**
 * Event to be handled in a ClusterService.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClusterNotificationEvent
    extends ClusterControlEvent {
    private static final long serialVersionUID = -8989354027230694379L;

    /**
     * Creates a new ClusterNotificationEvent object.
     *
     * @param source JAVADOC.
     * @param nodeId JAVADOC.
     */
    public ClusterNotificationEvent(String source, String nodeId) {
        super(source, nodeId);
    }

    /**
     * private constructor for the benefit of deserializing.
     */
    private ClusterNotificationEvent() {
        super("default");
    }
}
