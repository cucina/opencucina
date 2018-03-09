package org.cucina.cluster;

import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface ActiveClusterNodeService {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName JAVADOC.
	 * @param nodeId    JAVADOC.
	 */
	void executeActive(String eventName, String nodeId, Map<Object, Object> properties);
}
