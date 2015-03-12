package org.cucina.i18n.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.BeanFactory;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.repository.MessageRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Tests MessageHelper tests.
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class MessageHelperTest {
    private static final Locale SUSSEX = new Locale("en", "GB", "SU");
    private static final Locale UK = Locale.UK;
    private static final Locale EN = Locale.ENGLISH;
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MessageRepository messageRepository = mock(MessageRepository.class);

        when(messageRepository.getDefaultLocale()).thenReturn(Locale.ENGLISH);

        BeanFactory beanFactory = mock(BeanFactory.class);

        when(beanFactory.getBean(MessageRepository.MESSAGE_REPOSITORY_ID))
            .thenReturn(messageRepository);

        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);
    }

    /**
     * Test derives locales correctly.
     */
    @Test
    public void testDeriveLocales() {
        List<Locale> locales = MessageHelper.getDerivedLocales(SUSSEX);

        assertNotNull("Cannot return null list", locales);
        assertEquals("Incorrect number of results", 3, locales.size());
        assertEquals("Should be SUSSEX", SUSSEX, locales.get(0));
        assertEquals("Should be UK", UK, locales.get(1));
        assertEquals("Should be EN", EN, locales.get(2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testDeriveLocalesWhenCached() {
        List<Locale> locales = MessageHelper.getDerivedLocales(SUSSEX);

        locales = MessageHelper.getDerivedLocales(SUSSEX);
        locales = MessageHelper.getDerivedLocales(SUSSEX);

        assertNotNull("Cannot return null list", locales);
        assertEquals("Incorrect number of results", 3, locales.size());
        assertEquals("Should be SUSSEX", SUSSEX, locales.get(0));
        assertEquals("Should be UK", UK, locales.get(1));
        assertEquals("Should be EN", EN, locales.get(2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetDefaultLocale() {
        assertEquals(DEFAULT_LOCALE, MessageHelper.getDefaultLocale());
    }

    /**
     * A locale is required, otherwise Exception.
     */
    @Test
    public void testLocaleRequired() {
        try {
            MessageHelper.getDerivedLocales(null);
        } catch (IllegalArgumentException e) {
            return;
        }

        fail("Should have thrown exception");
    }
}