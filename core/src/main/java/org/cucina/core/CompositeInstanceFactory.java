package org.cucina.core;

import org.cucina.core.utils.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CompositeInstanceFactory
		extends AbstractInstanceFactory {
	private static final Logger LOG = LoggerFactory.getLogger(CompositeInstanceFactory.class);
	private List<InstanceFactory> instanceFactories = new ArrayList<InstanceFactory>();
	private Map<String, String> ptCache = new HashMap<String, String>();

	/**
	 * Creates a new CompositeInstanceFactory object.
	 */
	public CompositeInstanceFactory() {
		// default constructor
	}

	/**
	 * Creates a new CompositeInstanceFactory object.
	 */
	public CompositeInstanceFactory(InstanceFactory... factories) {
		instanceFactories = Arrays.asList(factories);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T>  JAVADOC.
	 * @param type JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public <T> T getBean(String type) {
		for (InstanceFactory instfac : instanceFactories) {
			@SuppressWarnings("unchecked")
			T t = (T) instfac.getBean(type);

			if (t != null) {
				return t;
			}
		}

		LOG.info("Failed to find bean with name '" + type + "'");

		return null;
	}

	public List<InstanceFactory> getInstanceFactories() {
		return instanceFactories;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param instanceFactories JAVADOC.
	 */
	public void setInstanceFactories(List<InstanceFactory> instanceFactories) {
		this.instanceFactories = instanceFactories;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param property JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public String getPropertyType(String className, String property) {
		String cacheKey = NameUtils.concat(className, property);
		String type = ptCache.get(cacheKey);

		if (type == null) {
			// If nested go down the hierarchy
			if (property.indexOf('.') > 0) {
				LOG.debug("Nester property name '" + property + "'");

				String latestPropertyName = property.substring(0, property.indexOf('.'));
				String latestType = getPropertyType(className, latestPropertyName);

				if (latestType == null) {
					LOG.debug("Could not workout nested Class type for " + property);

					return null;
				}

				String nextPropertyName = property.substring(property.indexOf('.') + 1);

				type = getPropertyType(latestType, nextPropertyName);
			} else {
				for (InstanceFactory instfac : instanceFactories) {
					type = instfac.getPropertyType(className, property);

					if (type != null) {
						break;
					}
				}
			}
		}

		if (type == null) {
			LOG.info("Failed to determine property type for name '" + property + "'");
		}

		ptCache.put(cacheKey, type);

		return type;
	}

	/**
	 * Assumes the fully qualified name. TODO make special cases for String and Date
	 *
	 * @param <T>  JAVADOC.
	 * @param name JAVADOC.
	 * @return JAVADOC.
	 */
	protected <T> Class<T> doGetClassType(String name) {
		for (InstanceFactory instfac : instanceFactories) {
			Class<T> clazz = instfac.getClassType(name);

			if (clazz != null) {
				return clazz;
			}
		}

		LOG.info("Failed to determine class for type name '" + name + "'");

		return null;
	}
}
