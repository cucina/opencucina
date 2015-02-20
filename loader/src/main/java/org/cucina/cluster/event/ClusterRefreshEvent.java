
package org.cucina.cluster.event;


/**
 * To be used to refresh the node's passive monitor
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ClusterRefreshEvent
    extends ClusterControlEvent {
    private static final long serialVersionUID = 5621880051490308439L;

    /**
     * Creates a new ClusterRefreshEvent object.
     *
     * @param source JAVADOC.
     * @param nodeId JAVADOC.
     */
    public ClusterRefreshEvent(String source, String nodeId) {
        super(source, nodeId);
    }

    /**
     * Creates a new ClusterRefreshEvent object.
     *
     * @param source JAVADOC.
     */
    public ClusterRefreshEvent(String source) {
        super(source);
    }
}
