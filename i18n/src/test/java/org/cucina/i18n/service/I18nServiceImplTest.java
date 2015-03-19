package org.cucina.i18n.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.cucina.core.service.ContextService;
import org.cucina.i18n.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: thornton $
 * @version $Revision: 1.11 $
 */
public class I18nServiceImplTest {
    private static final long DATE_MILLIS = 1201885200366L; //01-jan-2008 17:00:00.0
    @Mock
    private ContextService contextService;
    private I18nServiceImpl i18nService;
    @Mock
    private MessageRepository messageRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        i18nService = new I18nServiceImpl(contextService);
        i18nService.setDefaultLocale(Locale.ENGLISH);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAdjustDateForUserTimeZone() {
        HashMap<Object, Object> clientInfo = new HashMap<Object, Object>();

        clientInfo.put(I18nServiceImpl.CLIENT_TIMEZONE, "EST");
        i18nService.setClientLocaleInfo(clientInfo);
        when(contextService.get(I18nService.CLIENT_TIMEZONE)).thenReturn("EST");
        assertNull("should be null", i18nService.adjustForUserTimeZone(null));

        Calendar adjustedDate = Calendar.getInstance();

        adjustedDate.setTime(i18nService.adjustForUserTimeZone(new Date(DATE_MILLIS)));
        assertEquals("adjusted date should be server(GMT) less 5hrs", 12,
            adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Date should not have become next day", 1, adjustedDate.get(Calendar.DATE));
        TimeZone.setDefault(TimeZone.getTimeZone("EST"));
        adjustedDate.setTime(i18nService.adjustForUserTimeZone(new Date(DATE_MILLIS)));
        assertEquals("date should be unadjusted as server and user timezone is the same", 17,
            adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Date should not have become next day", 1, adjustedDate.get(Calendar.DATE));
        clientInfo.put(I18nServiceImpl.CLIENT_TIMEZONE, "GMT-7");
        i18nService.setClientLocaleInfo(clientInfo);
        when(contextService.get(I18nService.CLIENT_TIMEZONE)).thenReturn("GMT-7");
        adjustedDate.setTime(i18nService.adjustForUserTimeZone(new Date(DATE_MILLIS)));
        assertEquals("Date should be server(EST)less 2hrs", 15,
            adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Date should not have become next day", 1, adjustedDate.get(Calendar.DATE));
        clientInfo.put(I18nService.CLIENT_TIMEZONE, "GMT+2");
        i18nService.setClientLocaleInfo(clientInfo);
        when(contextService.get(I18nService.CLIENT_TIMEZONE)).thenReturn("GMT+2");
        adjustedDate.setTime(i18nService.adjustForUserTimeZone(new Date(DATE_MILLIS)));
        assertEquals("Date should be server(EST)plus 7hrs", 0,
            adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Date should have become next day", 2, adjustedDate.get(Calendar.DATE));

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
        clientInfo.put(I18nService.CLIENT_TIMEZONE, "GMT-5");
        i18nService.setClientLocaleInfo(clientInfo);
        when(contextService.get(I18nService.CLIENT_TIMEZONE)).thenReturn("GMT-5");
        adjustedDate.setTime(i18nService.adjustForUserTimeZone(new Date(DATE_MILLIS)));
        assertEquals("Hrs should be adjusted by -8hrs", 9, adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Day should change to following day", 1, adjustedDate.get(Calendar.DATE));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAdjustUserInputForServerTimeZone() {
        HashMap<Object, Object> clientInfo = new HashMap<Object, Object>();

        clientInfo.put(I18nService.CLIENT_TIMEZONE, "GMT+3");
        i18nService.setClientLocaleInfo(clientInfo);
        assertNull("should be null", i18nService.adjustUserInputForServerTimeZone(null));
        assertEquals(TimeZone.getTimeZone("GMT"), Calendar.getInstance().getTimeZone());
        when(contextService.get(I18nService.CLIENT_TIMEZONE)).thenReturn("GMT+3");

        Calendar adjustedDate = Calendar.getInstance();

        adjustedDate.setTime(i18nService.adjustUserInputForServerTimeZone(
                new Timestamp(DATE_MILLIS)));
        assertEquals("Hrs should be adjusted by -3hrs", 14, adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Day should not change", 1, adjustedDate.get(Calendar.DATE));

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
        clientInfo.put(I18nService.CLIENT_TIMEZONE, "GMT-5");
        i18nService.setClientLocaleInfo(clientInfo);
        when(contextService.get(I18nService.CLIENT_TIMEZONE)).thenReturn("GMT-5");
        adjustedDate.setTime(i18nService.adjustUserInputForServerTimeZone(new Date(DATE_MILLIS)));
        assertEquals("Hrs should be adjusted by +8hrs", 1, adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Day should change to following day", 2, adjustedDate.get(Calendar.DATE));
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-12"));
        clientInfo.put(I18nService.CLIENT_TIMEZONE, "GMT");
        i18nService.setClientLocaleInfo(clientInfo);
        when(contextService.get(I18nService.CLIENT_TIMEZONE)).thenReturn("GMT");
        adjustedDate.setTime(i18nService.adjustUserInputForServerTimeZone(new Date(DATE_MILLIS)));
        assertEquals("Hrs should be adjusted by -12", 5, adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Day should change to following day", 1, adjustedDate.get(Calendar.DATE));
        adjustedDate.setTimeInMillis(DATE_MILLIS);
        adjustedDate.set(Calendar.HOUR_OF_DAY, 11);
        adjustedDate.set(Calendar.MINUTE, 59);
        adjustedDate.setTime(i18nService.adjustUserInputForServerTimeZone(adjustedDate.getTime()));
        assertEquals("Hrs should be adjusted by -12", 23, adjustedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Day should change to previous day", 31, adjustedDate.get(Calendar.DATE));
        assertEquals("Month should change to previous month", 0, adjustedDate.get(Calendar.MONTH));
        assertEquals("Year should change to previous year", 2008, adjustedDate.get(Calendar.YEAR));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetCalendar() {
        Calendar cal = i18nService.getCalendar();

        assertNotNull("Should have a calendar instance", cal);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetDefaultLocale() {
        assertEquals(Locale.ENGLISH, i18nService.getDefaultLocale());
        i18nService.setDefaultLocaleString(Locale.KOREAN.toString());
        assertEquals(Locale.KOREAN, i18nService.getDefaultLocale());
    }

    /**
     * Order of precedence for locale: 1. Browser settings 2. Default locale for app.
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetLocale() {
        assertEquals(Locale.ENGLISH, i18nService.getLocale());

        HashMap<Object, Object> clientInfo = new HashMap<Object, Object>();

        clientInfo.put(I18nService.CLIENT_LOCALE, Locale.US);
        i18nService.setClientLocaleInfo(clientInfo);
        when(contextService.get(I18nService.CLIENT_LOCALE)).thenReturn(Locale.US);

        assertEquals(Locale.US, i18nService.getLocale());
    }

    /**
     * Test the functionality of <code>getTimeZone</code>
     */
    @Test
    public void testGetTimeZone() {
        assertEquals("GMT", i18nService.getTimeZone().getID());

        HashMap<Object, Object> clientInfo = new HashMap<Object, Object>();

        clientInfo.put(I18nService.CLIENT_TIMEZONE, "EST");
        i18nService.setClientLocaleInfo(clientInfo);
        when(contextService.get(I18nService.CLIENT_TIMEZONE)).thenReturn("EST");
        assertEquals("EST", i18nService.getTimeZone().getID());
    }
}
