package org.cucina.cluster;

import org.cucina.cluster.event.ClusterProcessEvent;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ClusterService {
	/**
	 * JAVADOC Method Level Comments
	 */
	void checkOutstanding();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 */
	void complete(String eventName);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	String myNodeId();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param nodeId JAVADOC.
	 * @return JAVADOC.
	 */
	boolean isMyNode(String nodeId);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 */
	void resetByHeartbeat(String eventName);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 */
	void handle(ClusterProcessEvent event);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 */
	void refresh();
}
