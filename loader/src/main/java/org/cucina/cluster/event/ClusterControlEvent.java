
package org.cucina.cluster.event;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.context.ApplicationEvent;


/**
 * Event to be handled in a ClusterService.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class ClusterControlEvent
    extends ApplicationEvent {
    private static final long serialVersionUID = -8989354027230694379L;
    private String applicationName;
    private String nodeId;

    // supers's source is transient, but we want ours to travel 
    private String source;

    /**
     * Creates a new ClusterControlEvent object.
     *
     * @param source JAVADOC.
     */
    public ClusterControlEvent(String source) {
        super(source);
        this.source = source;
    }

    /**
    * Creates a new ExecutorsLifecycleEvent object.
    *
    * @param source
    *            JAVADOC.
    */
    public ClusterControlEvent(String source, String nodeId) {
        super(source);
        this.source = source;
        this.nodeId = nodeId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param applicationName JAVADOC.
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @return JAVADOC.
    */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public Object getSource() {
        return source;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param obj
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        ClusterControlEvent ev = (ClusterControlEvent) obj;

        return new EqualsBuilder().append(getSource(), ev.getSource()).append(nodeId, ev.getNodeId())
                                  .isEquals();
    }

    //TODO implement hashcode
    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("eventName", source).append("nodeId", nodeId)
                                        .append("application.name", applicationName).toString();
    }
}
