
package org.cucina.cluster.event;



/**
 * Event to be handled in a ClusterService.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClusterProcessEvent
    extends ClusterPropertiesEvent {
    private static final long serialVersionUID = -8989354027230694379L;
   
    /**
    * Creates a new ClusterProcessEvent object.
    *
    * @param source JAVADOC.
    */
    public ClusterProcessEvent(String source) {
        super(source);
    }

    /**
     * Creates a new ClusterProcessEvent object.
     *
     * @param source JAVADOC.
     * @param nodeId JAVADOC.
     */
    public ClusterProcessEvent(String source, String nodeId) {
        super(source, nodeId);
    }

}
