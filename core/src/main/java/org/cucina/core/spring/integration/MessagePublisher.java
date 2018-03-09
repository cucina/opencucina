package org.cucina.core.spring.integration;

import java.util.Map;


/**
 * Bridge between application code and publishing functionality.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface MessagePublisher {
	/**
	 * Publish payload.
	 *
	 * @param payload Object.
	 */
	void publish(Object payload);

	/**
	 * Publish payload with headers.
	 *
	 * @param payload Object.
	 * @param headers Map<String, Object>.
	 */
	void publish(Object payload, Map<String, Object> headers);
}
