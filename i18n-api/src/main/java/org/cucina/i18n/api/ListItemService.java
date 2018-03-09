package org.cucina.i18n.api;

import java.util.Collection;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 */
public interface ListItemService {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param id JAVADOC.
	 * @return JAVADOC.
	 */
	ListItemDto load(Long id);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param type JAVADOC.
	 * @return JAVADOC.
	 */
	Collection<ListItemDto> loadByType(String type);

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param listNodeDto JAVADOC.
	 * @return JAVADOC.
	 */
	Long save(ListItemDto listNodeDto);
}
