package org.cucina.search.query.projection;

import org.cucina.search.query.Join;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Tests that TranslatedMessageWithJoinProjection functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TranslatedMessageWithJoinProjectionTest {
	private static final String ROOT_ALIAS = "rootAlias";
	private static final String ALIAS = "my";

	/**
	 * Tests that projection is as expected.
	 */
	@Test
	public void getProjection() {
		TranslatedMessageWithJoinProjection projection = new TranslatedMessageWithJoinProjection("bars.name",
				ALIAS, ROOT_ALIAS);

		projection.setParentAliases(Collections.singletonMap("bars", "parent"));
		assertEquals("Incorrect name", "parent.messageTx as my", projection.getProjection());
	}

	/**
	 * Tests that search property name is concantanted as expected
	 */
	@Test
	public void getSearchPropertyName() {
		TranslatedMessageWithJoinProjection projection = new TranslatedMessageWithJoinProjection("bars.name",
				ALIAS, ROOT_ALIAS);

		projection.setParentAliases(Collections.singletonMap("bars", "parent"));
		assertEquals("Incorrect name", "parent.messageTx", projection.getSearchPropertyName());
	}

	/**
	 * Test that nested property has expected and additional joins
	 */
	@Test
	public void testJoinsNested() {
		TranslatedMessageWithJoinProjection projection = new TranslatedMessageWithJoinProjection("bars.name",
				ALIAS, ROOT_ALIAS);
		Map<String, List<Join>> distinctJoins = projection.getJoins();

		assertNotNull(distinctJoins);
		assertEquals("Incorrect number of distinct joins", 1, distinctJoins.size());

		List<Join> joins = distinctJoins.get(ROOT_ALIAS);

		assertEquals("Incorrect number of joins", 3, joins.size());

		Join join = joins.get(0);

		assertEquals("Incorrect property", "bars", join.getProperty());
		join = joins.get(1);
		assertEquals("Incorrect property", "name", join.getProperty());
		join = joins.get(2);
		assertEquals("Incorrect property", "internationalisedMessages", join.getProperty());
	}

	/**
	 * Test that non nested property has additional joins added
	 */
	@Test
	public void testJoinsSimple() {
		TranslatedMessageWithJoinProjection projection = new TranslatedMessageWithJoinProjection("name",
				ALIAS, ROOT_ALIAS);
		Map<String, List<Join>> distinctJoins = projection.getJoins();

		assertNotNull(distinctJoins);
		assertEquals("Incorrect number of distinct joins", 1, distinctJoins.size());

		List<Join> joins = distinctJoins.get(ROOT_ALIAS);

		assertEquals("Incorrect number of joins", 2, joins.size());

		Join join = joins.get(0);

		assertEquals("Incorrect property", "name", join.getProperty());
		join = joins.get(1);
		assertEquals("Incorrect property", "internationalisedMessages", join.getProperty());
	}
}
