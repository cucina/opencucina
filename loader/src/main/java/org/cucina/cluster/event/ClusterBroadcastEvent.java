
package org.cucina.cluster.event;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ClusterBroadcastEvent
    extends ClusterPropertiesEvent {
    private static final long serialVersionUID = -7560055835582107336L;

    /**
     * Creates a new ClusterBroadcastEvent object.
     *
     * @param source JAVADOC.
     * @param nodeId JAVADOC.
     */
    public ClusterBroadcastEvent(String source, String nodeId) {
        super(source, nodeId);
    }

    /**
     * Creates a new ClusterBroadcastEvent object.
     *
     * @param source JAVADOC.
     */
    public ClusterBroadcastEvent(String source) {
        super(source);
    }

    /**
    * For deserializing
    */
    private ClusterBroadcastEvent() {
        super("default");
    }

    /**
         * Default toString implementation
         *
         * @return This object as String.
         */
    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                                        .append("properties", getProperties()).toString();
    }
}
