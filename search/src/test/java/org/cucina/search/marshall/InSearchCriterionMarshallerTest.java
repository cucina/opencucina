package org.cucina.search.marshall;

import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class InSearchCriterionMarshallerTest {
	private static final String NAME = "name";
	private static final String ALIAS = "alias";
	private static final String ROOT_ALIAS = "root";
	private static final Collection<String> VALUE = Collections.singleton("a");
	private InSearchCriterionMarshaller marshaller;

	/**
	 * Expects alias
	 */
	@Test(expected = IllegalArgumentException.class)
	public void checksAlias() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		InSearchCriterion criterion = new InSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

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
		InSearchCriterion criterion = new InSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

		marshaller.marshall(ALIAS, null, criterion, null);
	}

	/**
	 * Marshalls value
	 */
	@Test
	public void marshallsValue() {
		Map<String, Object> marshalledCriterion = new HashMap<String, Object>();

		InSearchCriterion criterion = new InSearchCriterion(NAME, null, ROOT_ALIAS, VALUE);

		marshaller.marshall(ALIAS, null, criterion, marshalledCriterion);

		@SuppressWarnings("unchecked")
		Map<String, Object> criteria = (Map<String, Object>) marshalledCriterion.get(ALIAS);

		assertEquals("Should have got value", VALUE.iterator().next(),
				((Collection<?>) criteria.get(SearchCriterionMarshaller.RESTRICTION_PROPERTY)).iterator()
						.next());
	}

	/**
	 * Set up for test
	 */
	@Before
	public void setup() {
		marshaller = new InSearchCriterionMarshaller();
	}

	/**
	 * Check supports for right type
	 */
	@Test
	public void supports() {
		assertTrue("Should support InTextSearchCriterion",
				marshaller.supports(InSearchCriterion.class));
		assertFalse("Shouldn't support NumberSearchCriterion",
				marshaller.supports(NumberSearchCriterion.class));
	}
}
