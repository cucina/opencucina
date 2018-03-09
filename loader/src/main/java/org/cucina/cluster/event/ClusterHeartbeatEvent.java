package org.cucina.cluster.event;


/**
 * Event to be handled in a ClusterService.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClusterHeartbeatEvent
		extends ClusterControlEvent {
	private static final long serialVersionUID = -8989354027230694379L;

	/**
	 * Creates a new ClusterHeartbeatEvent object.
	 *
	 * @param source JAVADOC.
	 * @param nodeId JAVADOC.
	 */
	public ClusterHeartbeatEvent(String source, String nodeId) {
		super(source, nodeId);
	}

	/**
	 * private constructor for the benefit of deserializing.
	 */
	private ClusterHeartbeatEvent() {
		super("default");
	}
}
