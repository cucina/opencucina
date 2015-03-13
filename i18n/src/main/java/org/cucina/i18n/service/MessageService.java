package org.cucina.i18n.service;

import java.util.Collection;
import java.util.Locale;

import org.cucina.i18n.MessageDto;
import org.cucina.i18n.model.Message;


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
     * @param page JAVADOC.
     * @param size JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<MessageDto> loadAll(int page, int size);

    /**
     * JAVADOC Method Level Comments
     * @param id
     *
     * @return JAVADOC.
     */
    Message loadById(Long id);

    /**
     * Load message by its code, locale and basename. Attempts to find message
     * with nearest locale first, then nearest basename.
     *
     * @param code
     *            .
     * @param locale
     * @param basenames
     *            ordered basenames.
     *
     * @return JAVADOC.
     */
    String loadMessage(String code, Locale locale, String... basenames);

    /**
     * Saves a new message
     * @param messageDto
     *
     * @return
     */
    boolean saveMessage(MessageDto messageDto);
}
