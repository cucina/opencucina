package org.cucina.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClusterNode {
	private static final Logger LOG = LoggerFactory.getLogger(ClusterServiceImpl.class);
	private UUID nodeId;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String myNodeId() {
		if (nodeId == null) {
			nodeId = UUID.randomUUID();
			LOG.debug("My node id=" + nodeId.toString());
		}

		return nodeId.toString();
	}
}
