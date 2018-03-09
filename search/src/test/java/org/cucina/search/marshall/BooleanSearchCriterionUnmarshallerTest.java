package org.cucina.search.marshall;

import org.cucina.search.query.criterion.BooleanSearchCriterion;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Test unmarshalling of DateSearchCriterionUnmarshaller functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BooleanSearchCriterionUnmarshallerTest {
	private static final String NAME = "name";
	private static final String ALIAS = "alias";
	private static final String ROOT_TYPE = "rootType";
	private static final String ROOT_ALIAS = "rootAlias";
	private BooleanSearchCriterionUnmarshaller unmarshaller;

	/**
	 * Set up for test.
	 */
	@Before
	public void setup() {
		unmarshaller = new BooleanSearchCriterionUnmarshaller();
	}

	/**
	 * unmarshall
	 */
	@Test
	public void unmarshall() {
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put(ALIAS, Boolean.TRUE);

		BooleanSearchCriterion searchCriterion = (BooleanSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertTrue("Should be true", searchCriterion.getValue());
	}

	/**
	 * unmarshall
	 */
	@Test
	public void unmarshallString() {
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put(ALIAS, "true");

		BooleanSearchCriterion searchCriterion = (BooleanSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertTrue("Should be true", searchCriterion.getValue());
	}

	/**
	 * unmarshall
	 */
	@Test
	public void unmarshallEmptyString() {
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put(ALIAS, "");

		assertNull("Should not have created criterion", unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria));
	}
}
