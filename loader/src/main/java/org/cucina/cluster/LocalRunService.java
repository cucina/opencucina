package org.cucina.cluster;

import org.cucina.cluster.event.ClusterPropertiesEvent;
import org.cucina.loader.agent.AgentRunner;

import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class LocalRunService {

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public abstract AgentRunner getExecutorRunner();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param eventName  JAVADOC.
	 * @param properties JAVADOC.
	 */
	public void run(String eventName, Map<Object, Object> properties) {
		getExecutorRunner().run(eventName, properties);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param event JAVADOC.
	 */
	public void onEvent(ClusterPropertiesEvent event) {
		String eventName = (String) event.getSource();
		run(eventName, event.getProperties());
	}
}
