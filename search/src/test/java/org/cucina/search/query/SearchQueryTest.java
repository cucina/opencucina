package org.cucina.search.query;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SearchQueryTest {
	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testCreateIn() {
		String ins = SearchQuery.createIn(10);

		assertEquals(" in(?,?,?,?,?,?,?,?,?,?)", ins);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testEqualsObject() {
		List<Object> values = new ArrayList<Object>();

		values.add("hehe");

		String queryText = "select this from that";
		SearchQuery query = new SearchQuery(queryText, values);

		assertFalse(query.equals("hehe"));
		assertFalse(query.equals(null));

		SearchQuery query2 = new SearchQuery("from that", values);

		assertFalse(query.equals(query2));
		query2 = new SearchQuery(queryText, (List<Object>) null);
		assertFalse(query.equals(query2));
		query2 = new SearchQuery(queryText, "hehe");
		assertTrue(query.equals(query2));
		assertEquals(values, query2.getValues());
		query2 = new SearchQuery(queryText, false, new Object[]{"hehe"});
		assertTrue(query.equals(query2));
		assertEquals(values, query2.getValues());
		query2 = new SearchQuery(queryText, true, new Object[]{"hehe"});
		assertFalse(query.equals(query2));
		assertEquals(values, query2.getValues());
		query2 = new SearchQuery(queryText, values, false);
		assertTrue(query.equals(query2));
		assertEquals(values, query2.getValues());
		assertEquals(queryText, query2.getQuery());

		assertTrue(query2.toString().contains("hehe"));
		assertTrue(query2.toString().contains(queryText));
	}
}
