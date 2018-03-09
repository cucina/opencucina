package org.cucina.cluster.event;

import org.springframework.context.ApplicationEvent;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public abstract class SendClusterEvent
		extends ApplicationEvent {
	/**
	 *
	 */
	private static final long serialVersionUID = 1538925118596885742L;

	/**
	 * Creates a new SendClusterEvent object.
	 *
	 * @param source JAVADOC.
	 */
	public SendClusterEvent(String source) {
		super(source);
	}
}
