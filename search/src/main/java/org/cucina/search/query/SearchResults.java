package org.cucina.search.query;

import java.util.List;
import java.util.Map;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface SearchResults {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T> JAVADOC.
	 * @return JAVADOC.
	 */
	<T> List<T> search();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T>   JAVADOC.
	 * @param start JAVADOC.
	 * @param max   JAVADOC.
	 * @return JAVADOC.
	 */
	<T> List<T> search(int start, int max);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param <T>   JAVADOC.
	 * @param <V>   JAVADOC.
	 * @param start JAVADOC.
	 * @param max   JAVADOC.
	 * @return JAVADOC.
	 */
	<T, V> List<Map<T, V>> searchMap(int start, int max);
}
