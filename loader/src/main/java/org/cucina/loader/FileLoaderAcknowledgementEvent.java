package org.cucina.loader;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class FileLoaderAcknowledgementEvent
		extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new FileLoaderAcknowledgementEvent object.
	 *
	 * @param container JAVADOC.
	 */
	public FileLoaderAcknowledgementEvent(UUID container) {
		super(container);
	}
}
