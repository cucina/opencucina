package org.cucina.security.bean;


/**
 * Provides domain object instances and type information.
 */
public interface InstanceFactory {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T>  JAVADOC.
	 * @param type JAVADOC.
	 * @return JAVADOC.
	 */
	<T> T getBean(String type);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T>  JAVADOC.
	 * @param type JAVADOC.
	 * @return JAVADOC.
	 */
	<T> Class<T> getClassType(String type);

	/**
	 * Returns whether or not this property is deemed to be a foreign key.
	 *
	 * @param property the name of an object property, as in
	 *                 <code>type.propertyName</code> to look up.
	 * @return true if it's a foreign key
	 */
	boolean isForeignKey(String className, String property);

	/**
	 * * Map property names to application types.
	 *
	 * @param className JAVADOC.
	 * @param property  JAVADOC.
	 * @return JAVADOC.
	 */
	String getPropertyType(String className, String property);
}
