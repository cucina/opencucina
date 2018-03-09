package org.cucina.search.marshall;

import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.criterion.InSearchCriterion;
import org.cucina.search.query.criterion.NumberSearchCriterion;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


/**
 * Test unmarshalling of DoubleSearchCriterionUnmarshaller functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class DoubleSearchCriterionUnmarshallerTest {
	private static final String NAME = "name";
	private static final String ALIAS = "alias";
	private static final String ROOT_TYPE = "rootType";
	private static final String ROOT_ALIAS = "rootAlias";
	private static final String PARENT_ALIAS = "parent";
	private static final Double FROM = 12.1d;
	private static final Double TO = 12.4d;
	private DoubleSearchCriterionUnmarshaller unmarshaller;

	/**
	 * Set up for test.
	 */
	@Before
	public void setup() {
		unmarshaller = new DoubleSearchCriterionUnmarshaller();
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
	 * unmarshall making sure that we get an instance of <code>Long</code>
	 */
	@Test
	public void unmarshallCollection() {
		Map<String, Object> criteria = new HashMap<String, Object>();

		Collection<Object> vals = new ArrayList<Object>();

		vals.add("100.0");
		vals.add(200d);
		criteria.put(ALIAS, vals);

		InSearchCriterion searchCriterion = (InSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

		searchCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set restriction correctly", PARENT_ALIAS + ".name in (?,?)",
				searchCriterion.getRestriction());
		assertTrue("Does not contain 100", searchCriterion.getValue().contains(100d));
		assertTrue("Does not contain 200", searchCriterion.getValue().contains(200d));
	}

	/**
	 * unmarshall
	 */
	@Test
	public void unmarshallDouble() {
		Map<String, Object> criteria = new HashMap<String, Object>();

		criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.FROM_PROPERTY), FROM);
		criteria.put(NameUtils.concat(ALIAS, SearchCriterionMarshaller.TO_PROPERTY), TO);

		NumberSearchCriterion searchCriterion = (NumberSearchCriterion) unmarshaller.unmarshall(NAME,
				ALIAS, ROOT_TYPE, ROOT_ALIAS, criteria);

		assertNotNull("searchCriterion must be returned", searchCriterion);
		assertEquals("not set from correctly", FROM, searchCriterion.getFrom());
		assertEquals("not set to correctly", TO, searchCriterion.getTo());
	}
}
