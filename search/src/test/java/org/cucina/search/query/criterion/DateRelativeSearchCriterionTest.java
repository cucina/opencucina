package org.cucina.search.query.criterion;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Test that the DateRelativeSearchCriterion functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DateRelativeSearchCriterionTest {
	private static final String NAME = "name";
	private static final String ROOT_ALIAS = "root";
	private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.DateRelativeSearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"from\":\"+1d\",\"to\":\"+10y\"}";
	private Marshaller marshaller;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void marshalls() {
		assertEquals("Should have marshalled correctly", MARSHALLED_PROJECTION,
				marshaller.marshall(
						new DateRelativeSearchCriterion(NAME, null, ROOT_ALIAS, "+1d", "+10y")));
	}

	/**
	 * Restriction should barf as is just a placeholder
	 */
	@Test(expected = IllegalArgumentException.class)
	public void restriction() {
		DateRelativeSearchCriterion criterion = new DateRelativeSearchCriterion(NAME, null,
				ROOT_ALIAS, "+1d", "+10y");

		criterion.getRestriction();
	}

	/**
	 * Sets up test
	 */
	@Before
	public void setup() {
		marshaller = new JacksonMarshaller(null, null);
	}

	/**
	 * Test from plus 1 day to plus 10 years.
	 */
	@Test
	public void testCriterion() {
		DateRelativeSearchCriterion criterion = new DateRelativeSearchCriterion(NAME, null,
				ROOT_ALIAS, "+1d", "+10y");

		assertEquals("incorrect from", "+1d", criterion.getFrom());
		assertEquals("incorrect to", "+10y", criterion.getTo());
	}

	/**
	 * Should barf as date from is after date to
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMinusMonthAndYear() {
		new DateRelativeSearchCriterion(NAME, null, ROOT_ALIAS, "-11m", "-5y");
	}

	/**
	 * Barf when from after to
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoSign() {
		new DateRelativeSearchCriterion(NAME, null, ROOT_ALIAS, "8y", "-5m");
	}

	/**
	 * Test from plus 1 day to plus 10 years.
	 */
	@Test
	public void testPlusDayAndYear() {
		DateRelativeSearchCriterion criterion = new DateRelativeSearchCriterion(NAME, null,
				ROOT_ALIAS, "+1d", "+10y");

		Calendar calFrom = getStartOfToday();

		calFrom.add(Calendar.DATE, 1);

		Calendar calTo = getEndOfToday();

		calTo.add(Calendar.YEAR, 10);

		check(criterion.getValues(), calFrom, calTo);
	}

	/**
	 * Test today produces from at start of day and to at end of day
	 */
	@Test
	public void testToday() {
		DateRelativeSearchCriterion criterion = new DateRelativeSearchCriterion(NAME, null,
				ROOT_ALIAS, DateRelativeHelper.TODAY, null);

		Calendar calFrom = getStartOfToday();
		List<Object> values = criterion.getValues();

		assertEquals("Should only have 1 value", 1, values.size());
		assertEquals("Should be start of today", calFrom.getTime(), values.iterator().next());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void unmarshalls() {
		DateRelativeSearchCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
				DateRelativeSearchCriterion.class);

		assertNotNull("Should have returned projection", unmarshalledCrit);
		assertEquals("Should have set name", NAME, unmarshalledCrit.getName());
		assertEquals("Should have set from", "+1d", unmarshalledCrit.getFrom());
		assertEquals("Should have set to", "+10y", unmarshalledCrit.getTo());
		assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
	}

	private Calendar getEndOfToday() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar;
	}

	private Calendar getStartOfToday() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	private void check(List<Object> dates, Calendar calFrom, Calendar calTo) {
		assertNotNull("Should have returned dates", dates);
		assertEquals("Incorrect number of dates", 2, dates.size());

		Iterator<Object> it = dates.iterator();
		Date from = (Date) it.next();
		Date to = (Date) it.next();

		Calendar comparison = Calendar.getInstance();

		comparison.setTime(from);

		assertEquals("From is incorrect", calFrom, comparison);

		comparison.setTime(to);

		assertEquals("To is incorrect", calTo, comparison);
	}
}
