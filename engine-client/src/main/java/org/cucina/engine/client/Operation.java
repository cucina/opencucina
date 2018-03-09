package org.cucina.engine.client;

import java.util.Map;


/**
 * @author vlevine
 */
public interface Operation {
	/**
	 * @param domain     .
	 * @param parameters .
	 */
	void execute(Object domain, Map<String, Object> parameters);
}
