package org.cucina.core.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Component(value = ContextService.CONTEXTSERVICE_ID)
public class ThreadLocalContextService
		implements ContextService {
	private ThreadLocal<Map<Object, Object>> threadLocal = new ThreadLocal<Map<Object, Object>>();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public Map<Object, Object> getAll() {
		return (getValue() == null) ? new HashMap<Object, Object>() : getValue();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Override
	public void clear() {
		threadLocal.remove();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param key JAVADOC.
	 * @return JAVADOC.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key) {
		Map<Object, Object> properties = getValue();

		if (properties != null) {
			return (T) properties.get(key);
		}

		return null;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param key   JAVADOC.
	 * @param value JAVADOC.
	 */
	@Override
	public void put(Object key, Object value) {
		Map<Object, Object> properties = getValue();

		if (properties == null) {
			properties = new HashMap<Object, Object>();
			setValue(properties);
		}

		properties.put(key, value);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param clientProps JAVADOC.
	 */
	@Override
	public void putAll(Map<Object, Object> clientProps) {
		Map<Object, Object> properties = getValue();

		if (properties == null) {
			properties = new HashMap<Object, Object>();
			setValue(properties);
		}

		properties.putAll(clientProps);
	}

	private Map<Object, Object> getValue() {
		return threadLocal.get();
	}

	private void setValue(Map<Object, Object> value) {
		threadLocal.set(value);
	}
}
