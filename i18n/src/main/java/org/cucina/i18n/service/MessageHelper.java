package org.cucina.i18n.service;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.util.Assert;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.repository.MessageRepository;


/**
 * Helper class which allows to group useful message related functionality.
 *
 * @author $author$
 * @version $Revision: 1.7 $
  */
public final class MessageHelper {
    /**
     * Return the system's default <code>Locale</code>
     * @return
     */
    public static Locale getDefaultLocale() {
        MessageRepository messageRepository = (MessageRepository) SingletonBeanFactory.getInstance()
                                                                                      .getBean(MessageRepository.MESSAGE_REPOSITORY_ID);

        Assert.notNull(messageRepository, "messageRepository is null");

        return messageRepository.getDefaultLocale();
    }

    /**
     * Provides User with a List of derived locales starting at the most specific, and ending at
     * the defaultLocale if it isn't already included in the list.
     *
     * @param locale required field.
     *
     * @return JAVADOC.
     */
    public static List<Locale> getDerivedLocales(Locale locale) {
        Assert.notNull(locale, "should supply locale argument");

        return LocaleUtils.localeLookupList(locale, getDefaultLocale());
    }

    /**
     * Returns a query that can be used to fetch messages for user's Locale,
     * degrades gracefully by returning the most specific message according to the
     * user's Locale, eventually returning the default message.
     * @param basename
     * @param code
     * @param includeCd
     * @return
     */
    public static String bestMsgJpql(String basename, String code, boolean includeCd) {
        Locale locale = ((I18nService) SingletonBeanFactory.getInstance()
                                                           .getBean(I18nService.I18N_SERVICE_ID)).getLocale();
        Locale defaultLocale = MessageHelper.getDefaultLocale();

        StringBuilder selectClause = new StringBuilder("SELECT\n");
        StringBuilder fromClause = new StringBuilder("FROM Message AS msg\n");

        fromClause.append("JOIN msg.internationalisedMessages AS d\n");

        if (includeCd) {
            selectClause.append("msg.messageCd,");
        }

        if (defaultLocale.equals(locale)) {
            selectClause.append("d.messageTx\n");
        } else {
            selectClause.append("CASE\n");
            fromClause.append("LEFT JOIN msg.internationalisedMessages AS lang\n");
            fromClause.append("ON lang.localeCd = '");
            fromClause.append(locale.getLanguage());
            fromClause.append("'\n");

            if (locale.getCountry().length() > 0) {
                selectClause.append("WHEN country.messageTx IS NULL\n");
                selectClause.append("THEN\n");
                selectClause.append("CASE\n");
                fromClause.append("LEFT JOIN msg.internationalisedMessages AS country\n");
                fromClause.append("ON country.localeCd = '");
                fromClause.append(locale.getLanguage());
                fromClause.append(((locale.getCountry().length()) > 0) ? ("_" +
                    locale.getCountry()) : "");
                fromClause.append("'\n");
            }

            selectClause.append("WHEN lang.messageTx IS NULL\n");
            selectClause.append("THEN d.messageTx\n");
            selectClause.append("ELSE lang.messageTx\n");

            if (locale.getCountry().length() > 0) {
                selectClause.append("END\n");
                selectClause.append("ELSE country.messageTx\n");
            }

            selectClause.append("END AS messageTx\n");
        }

        fromClause.append("WHERE d.localeCd     = '");
        fromClause.append(defaultLocale.getLanguage());
        fromClause.append(((defaultLocale.getCountry().length()) > 0)
            ? ("_" + defaultLocale.getCountry()) : "");
        fromClause.append("'\n");
        fromClause.append("AND msg.baseName     = '");
        fromClause.append(basename);
        fromClause.append("'");

        if (StringUtils.isNotBlank(code)) {
            fromClause.append("\n");
            fromClause.append("AND msg.messageCd = ");
            fromClause.append(code);
        }

        selectClause.append(fromClause);

        return selectClause.toString();
    }
}
