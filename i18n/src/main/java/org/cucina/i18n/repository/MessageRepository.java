package org.cucina.i18n.repository;

import java.util.Collection;
import java.util.Locale;

import org.cucina.i18n.model.Message;


/**
 * Interface for persistence operations on message bundles. The basic units of
 * data are the basename and a collection of message maps. The basename groups
 * messages together; it is like a resource bundle name (dot separated, e.g.
 * "messages.button"). The message map is a map consisting of keys:
 *
 * <pre>
 *  BASENAME, CODE, LOCALE, MESSAGE
 * </pre>
 *
 * which see below.
 *

 *
 */
public interface MessageRepository {
    /**
     * The key for basename in a message map, example value: "messages.domain".
     */
    String BASENAME = "BASE_NAME";

    /**
     * The key for message code in a message map, example value: "label.help".
     */
    String CODE = "MESSAGE_CD";

    /**
     * The key for locale in a message map. The values should be convertible to
     * {@link java.util.Locale}, and use the standard format of
     * {@link Locale#toString()}, e.g. "en_GB".
     */
    String LOCALE = "LOCALE_CD";

    /**
     * The key for message text in a message map, example value: "Make a {0}
     * noise here".
     */
    String MESSAGE = "MESSAGE_TX";

    /**
     * Get the default {@link Locale} for the application.
     *
     * @return default locale
     */
    Locale getDefaultLocale();

    /**
     * Returns the <code>Message</code> with the provided id.
     * @param id
     * @return Message
     */
    Message find(Long id);

    /**
     * JAVADOC Method Level Comments
     *
     * @param basename JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Load all the messages with the given basename.
     *
     * @param basename
     *            The basename of the message bundle, e.g. "messages.label"
     * @return collection of maps
     */
    Collection<Message> findByBasename(String basename);

    /**
     * JAVADOC.
     *
     * @param basename
     *            JAVADOC.
     * @param code
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    Message findByBasenameAndCode(String basename, String code);

    /**
     * JAVADOC Method Level Comments
     *
     * @param basenames JAVADOC.
     * @param code JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * JAVADOC.
     *
     * @param basenames
     *            JAVADOC.
     * @param code
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    Collection<Message> findByBasenamesAndCode(Collection<String> basenames, String code);

    /**
     * JAVADOC Method Level Comments
     *
     * @param code JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Load all messages for the given code from all basenames.
     *
     * @param code
     *            .
     *
     * @return Collection of maps.
     */
    Collection<Message> findByCode(String code);

    /**
     *
     * @param basename
     *            The basename of the message bundle, e.g. "messages.label"
     * @param locale
     *            The locale of the message.
     * @param code
     *            The key for the message.
     * @param msg
     *            The message translation for this locale.
     * @return the message that was saved if successful, null otherwise
     */
    Message save(String basename, String locale, String code, String msg);

    /**
     * JAVADOC Method Level Comments
     *
     * @param messages JAVADOC.
     *
     * @return JAVADOC.
     */
    /**
     * Save or update the messages in the input.
     *
     * @param messages
     *            A collection of maps.
     * @return the same content (might be different collection).
     */
    Collection<Message> save(Collection<Message> messages);

    /**
     * Save or update the message in the input.
     *
     * @param message
     *            to be saved.
     *
     * @return the same content.
     */
    Message save(Message message);
}
