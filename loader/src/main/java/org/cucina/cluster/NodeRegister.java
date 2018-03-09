package org.cucina.cluster;

import org.cucina.cluster.model.ClusterControl;

import java.util.Collection;
import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface NodeRegister {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 */
	void add(String eventName);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	Collection<ClusterControl> outstandingEvents(String nodeId);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 */
	void remove(String eventName);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 */
	void reset(String eventName);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 * @param nodeId    JAVADOC.
	 * @return JAVADOC.
	 */
	boolean switchActivePassive(String eventName, String nodeId, Map<Object, Object> properties);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param myNodeId JAVADOC.
	 * @return
	 */
	Collection<String> refresh(String myNodeId);
}
