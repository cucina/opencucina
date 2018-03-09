package org.cucina.search;


/**
 * Restriction for <code>Map</code> attributes properties.
 */
public interface AttributeRestrictor {
	/**
	 * Returns true if this matcher can handle the attribute type.
	 *
	 * @param clazz
	 * @return
	 */
	boolean supports(Class<?> clazz);

	/**
	 * Tests whether or not this attribute property is is valid for the provided
	 * restriction.
	 *
	 * @param attribute   the attribute value to test
	 * @param restriction value to test the attribute against.
	 * @return
	 */
	boolean test(Object attribute, Object restriction);
}
