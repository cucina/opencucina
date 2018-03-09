package org.cucina.loader.agent;

import java.util.Collection;


/**
 * JAVADOC Interface Level
 *
 * @param <T> JAVADOC.
 * @author $Author: $
 * @version $Revision: $
 */
public interface ParallelAgent<T> {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	Collection<T> obtainItems();

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param item JAVADOC.
	 */
	void processItem(T item);
}
