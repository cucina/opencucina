package org.cucina.search.query.projection;

import java.util.Collections;
import java.util.Locale;

import org.springframework.beans.factory.BeanFactory;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.i18n.service.I18nService;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * Test that we generate the correct projection.
 *

 */
public class TranslatedPropertyProjectionTest {
    private static final String COUNTRY_AND_LANG_HQL = "(coalesce((SELECT\n" + "CASE\n" +
        "WHEN country.messageTx IS NULL\n" + "THEN\n" + "CASE\n" + "WHEN lang.messageTx IS NULL\n" +
        "THEN d.messageTx\n" + "ELSE lang.messageTx\n" + "END\n" + "ELSE country.messageTx\n" +
        "END AS messageTx\n" + "FROM Message AS msg\n" +
        "JOIN msg.internationalisedMessages AS d\n" +
        "LEFT JOIN msg.internationalisedMessages AS lang\n" + "ON lang.localeCd = 'fr'\n" +
        "LEFT JOIN msg.internationalisedMessages AS country\n" + "ON country.localeCd = 'fr_FR'\n" +
        "WHERE d.localeCd     = 'en'\n" + "AND msg.baseName     = '" +
        TranslatedPropertyProjection.BASENAME + "'\n" +
        "AND msg.messageCd = foo.name),foo.name)) as orderedName";
    private static final String LANG_HQL = "(coalesce((SELECT\n" + "CASE\n" +
        "WHEN lang.messageTx IS NULL\n" + "THEN d.messageTx\n" + "ELSE lang.messageTx\n" +
        "END AS messageTx\n" + "FROM Message AS msg\n" +
        "JOIN msg.internationalisedMessages AS d\n" +
        "LEFT JOIN msg.internationalisedMessages AS lang\n" + "ON lang.localeCd = 'fr'\n" +
        "WHERE d.localeCd     = 'en'\n" + "AND msg.baseName     = '" +
        TranslatedPropertyProjection.BASENAME + "'\n" +
        "AND msg.messageCd = foo.name),foo.name)) as orderedName";
    private static final String SIMPLE_HQL = "(coalesce((SELECT\n" + "d.messageTx\n" +
        "FROM Message AS msg\n" + "JOIN msg.internationalisedMessages AS d\n" +
        "WHERE d.localeCd     = 'en'\n" + "AND msg.baseName     = '" +
        TranslatedPropertyProjection.BASENAME + "'\n" +
        "AND msg.messageCd = foo.name),foo.name)) as orderedName";
    @Mock
    private I18nService i18nService;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(i18nService.getDefaultLocale()).thenReturn(Locale.ENGLISH);

        BeanFactory beanFactory = mock(BeanFactory.class);

        when(beanFactory.getBean(I18nService.I18N_SERVICE_ID)).thenReturn(i18nService);

        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testLangOnly() {
        when(i18nService.getLocale()).thenReturn(Locale.FRENCH);

        TranslatedPropertyProjection proj = new TranslatedPropertyProjection("name", "orderedName",
                "foo");

        proj.setParentAliases(Collections.singletonMap("foo", "foo"));

        assertEquals(LANG_HQL, proj.getProjection());
        verify(i18nService).getLocale();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSameAsDefault() {
        when(i18nService.getLocale()).thenReturn(Locale.ENGLISH);

        TranslatedPropertyProjection proj = new TranslatedPropertyProjection("name", "orderedName",
                "foo");

        proj.setParentAliases(Collections.singletonMap("foo", "foo"));

        assertEquals(SIMPLE_HQL, proj.getProjection());
        verify(i18nService).getLocale();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testWithCountry() {
        when(i18nService.getLocale()).thenReturn(Locale.FRANCE);

        TranslatedPropertyProjection proj = new TranslatedPropertyProjection("name", "orderedName",
                "foo");

        proj.setParentAliases(Collections.singletonMap("foo", "foo"));

        assertEquals(COUNTRY_AND_LANG_HQL, proj.getProjection());
        verify(i18nService).getLocale();
    }
}
