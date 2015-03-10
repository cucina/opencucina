package org.cucina.i18n.service;

import java.util.Locale;


/**
 * JAVADOC.
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public interface MessageLoader {
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
     * @param text
     * @param code
     * @param locale
     * @param applicationName
     * @return
     */
    boolean saveMessage(String text, String code, Locale locale, String applicationName);
}
