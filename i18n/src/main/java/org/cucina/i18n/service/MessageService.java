package org.cucina.i18n.service;

import java.util.Collection;
import java.util.Locale;

import org.cucina.i18n.api.MessageDto;

/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public interface MessageService {
	/**
	 * Load all messages
	 *
	 * @return JAVADOC.
	 */
	Collection<MessageDto> loadAll();

	/**
	 * Load all with paging
	 *
	 * @param page
	 *            JAVADOC.
	 * @param size
	 *            JAVADOC.
	 *
	 * @return JAVADOC.
	 */
	Collection<MessageDto> loadAll(int page, int size);

	/**
	 * JAVADOC Method Level Comments
	 * 
	 * @param id
	 *
	 * @return JAVADOC.
	 */
	MessageDto loadById(Long id, Locale locale);

	/**
	 * Load message by its code, locale and basename. Attempts to find message
	 * with nearest locale.
	 *
	 * @param code
	 * @param locale
	 * @param basename
	 *
	 * @return JAVADOC.
	 */
	String loadMessage(String code, Locale locale, String basename);

	/**
	 * Saves a new message
	 * 
	 * @param messageDto
	 *
	 * @return
	 */
	Long saveMessage(MessageDto messageDto);
}
