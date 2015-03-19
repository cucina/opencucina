package org.cucina.i18n.api;

import java.util.Collection;

import org.cucina.i18n.api.ListNodeDto;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
  */
public interface ListNodeService {
    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     *
     * @return JAVADOC.
     */
    ListNodeDto load(Long id);

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<ListNodeDto> loadByType(String type);

    /**
     * JAVADOC Method Level Comments
     *
     * @param listNodeDto JAVADOC.
     *
     * @return JAVADOC.
     */
    Long save(ListNodeDto listNodeDto);
}
