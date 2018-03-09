package org.cucina.search;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import java.util.Collection;


/**
 * Lenient checking of simple <code>Collection</code> attributes, returns true
 * if any of the restriction values are present in the attribute value.
 */
public class CollectionAttributeRestrictor
		implements AttributeRestrictor {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param clazz JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Collection.class.isAssignableFrom(clazz);
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param attribute   JAVADOC.
	 * @param restriction JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public boolean test(Object attribute, Object restriction) {
		boolean match = false;

		Assert.isInstanceOf(Collection.class, attribute, "attribute must be a Collection!");
		Assert.notNull(restriction, "restriction must be provided!");

		if (restriction instanceof Collection) {
			match = CollectionUtils.containsAny((Collection<?>) attribute,
					(Collection<?>) restriction);
		} else {
			match = ((Collection<?>) attribute).contains(restriction);
		}

		return match;
	}
}
