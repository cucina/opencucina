package org.cucina.cluster.event;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClusterAttemptCompleteEvent
		extends ClusterPropertiesEvent {
	/**
	 *
	 */
	private static final long serialVersionUID = -6295558236974460179L;

	/**
	 * Creates a new ClusterAttemptCompleteEvent object.
	 *
	 * @param source JAVADOC.
	 * @param nodeId JAVADOC.
	 */
	public ClusterAttemptCompleteEvent(String source, String nodeId) {
		super(source, nodeId);
	}

	/**
	 * Creates a new ClusterAttemptCompleteEvent object.
	 *
	 * @param source JAVADOC.
	 */
	public ClusterAttemptCompleteEvent(String source) {
		super(source);
	}

	/**
	 * For deserializing
	 */
	private ClusterAttemptCompleteEvent() {
		super("default");
	}
}
