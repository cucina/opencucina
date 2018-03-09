package org.cucina.search.query.projection;

import org.cucina.i18n.api.LocaleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Test that we generate the correct projection.
 */
public class TranslatedPropertyProjectionTest {
	private static final String COUNTRY_AND_LANG_HQL = "(coalesce((SELECT\n" + "CASE\n" +
			"WHEN country.messageTx IS NULL\n" + "THEN\n" + "CASE\n" + "WHEN lang.messageTx IS NULL\n" +
			"THEN d.messageTx\n" + "ELSE lang.messageTx\n" + "END\n" + "ELSE country.messageTx\n" +
			"END AS messageTx\n" + "FROM Message AS msg\n" +
			"JOIN msg.internationalisedMessages AS d\n" +
			"LEFT JOIN msg.internationalisedMessages AS lang\n" + "ON lang.localeCd = 'fr'\n" +
			"LEFT JOIN msg.internationalisedMessages AS country\n" + "ON country.localeCd = 'fr_FR'\n" +
			"WHERE d.localeCd     = '" + Locale.getDefault().toString() + "'\n" +
			"AND msg.baseName     = '" + TranslatedPropertyProjection.BASENAME + "'\n" +
			"AND msg.messageCd = foo.name),foo.name)) as orderedName";
	private static final String LANG_HQL = "(coalesce((SELECT\n" + "CASE\n" +
			"WHEN lang.messageTx IS NULL\n" + "THEN d.messageTx\n" + "ELSE lang.messageTx\n" +
			"END AS messageTx\n" + "FROM Message AS msg\n" +
			"JOIN msg.internationalisedMessages AS d\n" +
			"LEFT JOIN msg.internationalisedMessages AS lang\n" + "ON lang.localeCd = 'fr'\n" +
			"WHERE d.localeCd     = '" + Locale.getDefault().toString() + "'\n" +
			"AND msg.baseName     = '" + TranslatedPropertyProjection.BASENAME + "'\n" +
			"AND msg.messageCd = foo.name),foo.name)) as orderedName";
	private static final String SIMPLE_HQL = "(coalesce((SELECT\n" + "d.messageTx\n" +
			"FROM Message AS msg\n" + "JOIN msg.internationalisedMessages AS d\n" +
			"WHERE d.localeCd     = '" + Locale.getDefault().toString() + "'\n" +
			"AND msg.baseName     = '" + TranslatedPropertyProjection.BASENAME + "'\n" +
			"AND msg.messageCd = foo.name),foo.name)) as orderedName";
	@Mock
	private LocaleService localeService;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testLangOnly() {
		when(localeService.currentUserLocale()).thenReturn(Locale.FRENCH);

		TranslatedPropertyProjection proj = new TranslatedPropertyProjection("name", "orderedName",
				"foo", localeService);

		proj.setParentAliases(Collections.singletonMap("foo", "foo"));

		assertEquals(LANG_HQL, proj.getProjection());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testSameAsDefault() {
		when(localeService.currentUserLocale()).thenReturn(Locale.getDefault());

		TranslatedPropertyProjection proj = new TranslatedPropertyProjection("name", "orderedName",
				"foo", localeService);

		proj.setParentAliases(Collections.singletonMap("foo", "foo"));

		assertEquals(SIMPLE_HQL, proj.getProjection());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testWithCountry() {
		when(localeService.currentUserLocale()).thenReturn(Locale.FRANCE);

		TranslatedPropertyProjection proj = new TranslatedPropertyProjection("name", "orderedName",
				"foo", localeService);

		proj.setParentAliases(Collections.singletonMap("foo", "foo"));

		assertEquals(COUNTRY_AND_LANG_HQL, proj.getProjection());
	}
}
