package org.cucina.search.marshall;

import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Test unmarshalling of LongSearchCriterionUnmarshaller functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class LongSearchCriterionUnmarshallerTest {
	private static final String NAME = "name";
	private static final String ALIAS = "alias";
	private static final String ROOT_TYPE = "rootType";
	private static final String ROOT_ALIAS = "rootAlias";
	private static final Long FROM = 12L;
	private static final Long TO = 13L;
	private LongSearchCriterionUnmarshaller unmarshaller;

	/**
	 * Set up for test.
	 */
	@Before
	public void setup() {
		unmarshaller = new LongSearchCriterionUnmarshaller();
	}

	/**
	 * unmarshall
	 */
	@Test
	public void unmarshall() {
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY),
				FROM.toString());
		criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY), TO.toString());

		NumberSearchCriterion searchCriterion = (NumberSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set from correctly", FROM, searchCriterion.getFrom());
		assertEquals("not set to correctly", TO, searchCriterion.getTo());
	}

	/**
	 * unmarshall
	 */
	@Test
	public void unmarshallLong() {
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY), FROM);
		criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY), TO);

		NumberSearchCriterion searchCriterion = (NumberSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set from correctly", FROM, searchCriterion.getFrom());
		assertEquals("not set to correctly", TO, searchCriterion.getTo());
	}

	/**
	 * unmarshall making sure that we get an instance of <code>Long</code>
	 */
	@Test
	public void unmarshallNumber() {
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY),
				FROM.intValue());
		criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY),
				TO.doubleValue());

		NumberSearchCriterion searchCriterion = (NumberSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set from correctly", FROM, searchCriterion.getFrom());
		assertEquals("not set to correctly", TO, searchCriterion.getTo());
	}
}
