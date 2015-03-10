package org.cucina.i18n.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.security.core.userdetails.User;


/**
 * Provides access to the current user's locale.
 *
 */
public interface I18nService {
    /**
     * Default id for the bean 'i18nFactory'
     */
    public static final String I18N_SERVICE_ID = "i18nService";

    /** locale. */
    public static final String CLIENT_LOCALE = "locale";

    /** client.timezone. */
    public static final String CLIENT_TIMEZONE = "client.timezone";

    /**
     * Provides access to an instance of a {@link Calendar} based on a
     * {@link User}'s chosen locale.
     *
     * @return a {@Calendar} instance initialised with the current
     *         {@User}'s locale, or the default Locale if no Locale
     *         preferences exist for the current {@link User}.
     */
    public Calendar getCalendar();

    /**
     * Sets the client info. <code>Map</code> contains locale=user's
     * <code>Locale</code> and timezone=user's timezoneoffset.
     *
     */
    public void setClientLocaleInfo(Map<Object, Object> clientInfo);

    /**
     * Provides access to the User's Locale. In practice should rarely be used,
     * instead consider using the convenience locale-aware accessors such as
     * <code>getCalendar</code>, <code>getCurrenyFormat</code>, etc... Possible
     * exceptions include <code>String.toLowerCase</code>...
     *
     * @return Return's the currently authenticated User's Locale
     */
    public Locale getLocale();

    /**
     * Provides access to the currently authenticated User's TimeZone
     *
     * @return the {@TimeZone} chosen by the {@User}, or the
     *         default TimeZone (where the server is running) if not
     */
    public TimeZone getTimeZone();

    /**
     * Adjust the date object for display to the user taking the users timezone
     * into account. If server timezone = GMT and date = 01/01/2008 17:00:00,
     * would be returned to user with timezone = EST as 01/01/2008 12:00:00.
     *
     * @param date
     *            the date to be adjusted.
     * @return Date adjusted for user timezone
     */
    public Date adjustForUserTimeZone(Date date);

    /**
     * Adjust the date object submittred by the user. If user timezone = EST and
     * date = 01/01/2008 12:00:00 and server timezone = GMT then result would be
     * 01/01/2008 12:00:00.
     *
     * @param date
     *            the date to be adjusted.
     * @return Date adjusted for server timezone
     */
    public Date adjustUserInputForServerTimeZone(Date date);
}
