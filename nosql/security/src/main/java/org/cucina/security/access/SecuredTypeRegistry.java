package org.cucina.security.access;

import java.util.Collection;


/**
 * Registry which holds the application types which
 * will be secured.
 */
public interface SecuredTypeRegistry {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	/**
	 * Returns the list of properties which are securable.
	 * Resulting list is in the form [propertyName].[propertyType]
	 *
	 * @return
	 */
	Collection<String> listSecurableProperties();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	/**
	 * Returns the types that will be secured.
	 *
	 * @return
	 */
	Collection<String> listSecuredTypes();
}
