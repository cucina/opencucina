package org.cucina.search;

import java.util.Collection;


/**
 * Projection appender which can be used to append a number of 'projections' to
 * the results.
 */
public interface ResultSetModifier {
	/**
	 * Returns a list of the properties that this modifier will add to the
	 * resultset.
	 *
	 * @param applicationType
	 */
	Collection<String> listProperties(String applicationType);

	/**
	 * Add projections to the results.
	 *
	 * @param results
	 */
	void modify(String applicationType, @SuppressWarnings("rawtypes")
			Collection results);
}
