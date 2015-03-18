package org.cucina.i18n.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import org.cucina.core.service.ContextService;

import org.cucina.i18n.repository.MessageRepository;


/**
 * Implementation which makes use of <code>ContextService</code> to get the
 * current <code>User</code>'s locale info.
 *
 */
@Service(value = "i18nService")
public class I18nServiceImpl
    implements I18nService {
    private ContextService contextService;
    private MessageRepository messageRepository;

    /**
     * Creates a new I18nServiceImpl object.
     *
     * @param messageRepository
     *            JAVADOC.
     * @param contextService
     *            JAVADOC.
     */
    @Autowired
    public I18nServiceImpl(MessageRepository messageRepository, ContextService contextService) {
        Assert.notNull(messageRepository, "messageRepository is null");
        this.messageRepository = messageRepository;
        Assert.notNull(contextService, "contextService is null");
        this.contextService = contextService;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     *
     * @see org.cucina.meringue.service.opvantage.core.service.I18nService#getCalendar()
     */
    public Calendar getCalendar() {
        return Calendar.getInstance(getTimeZone(), getLocale());
    }

    /**
     * JAVADOC.
     *
     * @param clientInfo
     *            JAVADOC.
     * @see org.cucina.meringue.service.opvantage.core.service.I18nService#setClientLocaleInfo(java.util.Map)
     */
    public void setClientLocaleInfo(Map<Object, Object> clientInfo) {
        contextService.putAll(clientInfo);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     * @see org.cucina.meringue.service.sprite.core.service.I18nService#getLocale()
     */
    public Locale getLocale() {
        Locale locale = (Locale) contextService.get(CLIENT_LOCALE);

        if (locale == null) {
            locale = messageRepository.getDefaultLocale();
        }

        return locale;
    }

    /**
     * Return the <code>TimeZone</code> for the user. First checks the users
     * preferences, then tries to get it from the client map. If both of these
     * are empty then take the JVM <code>TimeZone</code>
     *
     * @return TimeZone
     * @see org.cucina.meringue.service.opvantage.core.service.I18nService#getTimeZone()
     */
    @Override
    public TimeZone getTimeZone() {
        String timezoneName = (String) contextService.get(CLIENT_TIMEZONE);
        TimeZone timezone = null;

        if (StringUtils.hasText(timezoneName)) {
            timezone = TimeZone.getTimeZone(timezoneName);
        }

        if (timezone == null) {
            timezone = Calendar.getInstance(getLocale()).getTimeZone();
        }

        return timezone;
    }

    /**
     * JAVADOC.
     *
     * @param date
     *            JAVADOC.
     *
     * @return JAVADOC.
     * @see org.cucina.meringue.service.opvantage.core.service.I18nService#adjustForUserTimeZone(java.util.Date)
     */
    public Date adjustForUserTimeZone(Date date) {
        if (date == null) {
            return date;
        }

        Date cloneOf = (Date) date.clone();
        long millis = cloneOf.getTime();

        cloneOf.setTime(millis - calculateDiffBetweenUserandServer(millis));

        return cloneOf;
    }

    /**
     * JAVADOC.
     *
     * @param date
     *            JAVADOC.
     *
     * @return JAVADOC.
     * @see org.cucina.meringue.service.opvantage.core.service.I18nService#adjustUserInputForServerTimeZone(java.util.Date)
     */
    public Date adjustUserInputForServerTimeZone(Date date) {
        if (date == null) {
            return date;
        }

        Date cloneOf = (Date) date.clone();
        long millis = date.getTime();

        cloneOf.setTime(millis + calculateDiffBetweenUserandServer(millis));

        return cloneOf;
    }

    /**
     * Calculates the offset between the user and the server.
     *
     * @param millis
     * @return offset
     */
    private long calculateDiffBetweenUserandServer(long millis) {
        long serverOffset = TimeZone.getDefault().getOffset(millis);
        long userOffset = getTimeZone().getOffset(millis);
        long userFromServer = 0;

        userFromServer = serverOffset - userOffset;

        return userFromServer;
    }
}
