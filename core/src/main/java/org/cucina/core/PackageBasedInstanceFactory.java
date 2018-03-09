package org.cucina.core;

import org.cucina.core.utils.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;


/**
 * Loads classes from the specified package
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PackageBasedInstanceFactory
		extends AbstractInstanceFactory {
	private static final Logger LOG = LoggerFactory.getLogger(PackageBasedInstanceFactory.class);
	private String packageName;

	/**
	 * Create new instanceFactory
	 *
	 * @param packageName
	 */
	public PackageBasedInstanceFactory(String packageName) {
		super();
		Assert.notNull(packageName, "packageName must be provided");
		this.packageName = packageName;
	}

	/**
	 * Assumes that type is a class name in org.cucina.meringue.model package
	 *
	 * @param <T>  JAVADOC.
	 * @param type JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public <T> T getBean(String type) {
		Class<T> clazz = getClassType(type);

		if (clazz == null) {
			LOG.info("Could not find the class for type '" + type + "'");

			return null;
		}

		return getBean(clazz);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T>  JAVADOC.
	 * @param name JAVADOC.
	 * @return JAVADOC.
	 */
	@SuppressWarnings("unchecked")
	protected <T> Class<T> doGetClassType(String name) {
		String className = NameUtils.concat(packageName, name);

		try {
			return (Class<T>) ClassUtils.forName(className, this.getClass().getClassLoader());
		} catch (ClassNotFoundException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Class not found [" + e.getLocalizedMessage() + "]");
			}
		}

		return null;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T>   JAVADOC.
	 * @param clazz JAVADOC.
	 * @return JAVADOC.
	 */
	private <T> T getBean(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			LOG.error("Oops [" + e.getMessage() + "]");
		} catch (IllegalAccessException e) {
			LOG.error("Oops", e);
		}

		return null;
	}
}
