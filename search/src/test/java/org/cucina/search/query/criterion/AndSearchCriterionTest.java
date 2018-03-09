package org.cucina.search.query.criterion;

import org.cucina.search.query.SearchCriterion;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class AndSearchCriterionTest {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetValues() {
		String rootAlias = "top";
		List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();
		String value = "x";
		SearchCriterion inner1 = mock(SearchCriterion.class);

		when(inner1.getValues()).thenReturn(Collections.singletonList((Object) value));

		criteria.add(inner1);

		String value2 = "y";
		String value3 = "z";

		List<Object> values = new ArrayList<Object>();

		values.add(value2);
		values.add(value3);

		SearchCriterion inner2 = mock(SearchCriterion.class);

		when(inner2.getValues()).thenReturn(values);

		criteria.add(inner2);

		AndSearchCriterion crit = new AndSearchCriterion(rootAlias, criteria);

		List<Object> retValues = crit.getValues();

		assertEquals(3, retValues.size());
		assertEquals("x", retValues.iterator().next());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testRestriction() {
		String rootAlias = "top";
		List<SearchCriterion> criteria = new ArrayList<SearchCriterion>();
		String restriction1 = "x = y";
		SearchCriterion inner1 = mock(SearchCriterion.class);

		when(inner1.getRestriction()).thenReturn(restriction1);

		criteria.add(inner1);

		String restriction2 = "z = a";
		SearchCriterion inner2 = mock(SearchCriterion.class);

		when(inner2.getRestriction()).thenReturn(restriction2);

		criteria.add(inner2);

		AndSearchCriterion crit = new AndSearchCriterion(rootAlias, criteria);

		String restriction = crit.getRestriction();

		assertNotNull(restriction);
		assertEquals(restriction, "(" + restriction1 + " and " + restriction2 + ")");
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testRestrictionEmptyCriteria() {
		String rootAlias = "top";

		AndSearchCriterion crit = new AndSearchCriterion(rootAlias,
				Collections.<SearchCriterion>emptyList());

		assertNotNull(crit.getRestriction());
		assertTrue(crit.getRestriction().length() == 0);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRestrictionNullCriteria() {
		String rootAlias = "top";

		@SuppressWarnings("unused")
		AndSearchCriterion crit = new AndSearchCriterion(rootAlias, null);
	}
}
