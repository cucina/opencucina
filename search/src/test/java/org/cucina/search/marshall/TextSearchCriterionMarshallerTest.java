package org.cucina.search.marshall;

import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.cucina.search.query.criterion.TextSearchCriterion;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Test that TextSearchCriterionMarshaller functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class TextSearchCriterionMarshallerTest {
	private static final String NAME = "name";
	private static final String ALIAS = "alias";
	private static final String ROOT_ALIAS = "root";
	private static final String VALUE = "value";
	private TextSearchCriterionMarshaller marshaller;

	/**
	 * Expects alias
	 */
	@Test(expected = IllegalArgumentException.class)
	public void checksAlias() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		TextSearchCriterion criterion = new TextSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

		marshaller.marshall(null, null, criterion, marshalledCriterion);
	}

	/**
	 * Expects criterion
	 */
	@Test(expected = IllegalArgumentException.class)
	public void checksCriterion() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		marshaller.marshall(ALIAS, null, null, marshalledCriterion);
	}

	/**
	 * Expects marshalledCriterion
	 */
	@Test(expected = IllegalArgumentException.class)
	public void checksMarshalledCriterion() {
		TextSearchCriterion criterion = new TextSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

		marshaller.marshall(ALIAS, null, criterion, null);
	}

	/**
	 * Marshalls value
	 */
	@Test
	public void marshallsValue() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		TextSearchCriterion criterion = new TextSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

		marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

		@SuppressWarnings("unchecked")
		Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

		assertEquals("Should have got value", VALUE,
				criteria.get(SearchCriterionMarshaller.RESTRICTION_PROPERTY));
	}

	/**
	 * Set up for test
	 */
	@Before
	public void setup() {
		marshaller = new TextSearchCriterionMarshaller();
	}

	/**
	 * Check supports for right type
	 */
	@Test
	public void supports() {
		assertTrue("Should support TextSearchCriterionMarshaller",
				marshaller.supports(TextSearchCriterion.class));
		assertFalse("Shouldn't support NumberSearchCriterion",
				marshaller.supports(NumberSearchCriterion.class));
	}
}
