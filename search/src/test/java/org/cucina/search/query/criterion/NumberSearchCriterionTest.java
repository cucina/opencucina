package org.cucina.search.query.criterion;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.core.utils.NameUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Tests NumberSearchCriterion functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NumberSearchCriterionTest {
	private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.NumberSearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"from\":[\"java.lang.Long\",12],\"to\":[\"java.lang.Long\",15]}";
	private static final Number FROM = new Long(12L);
	private static final Number TO = new Long(15L);
	private static final String PROPERTY = "name";
	private static final String PARENT_ALIAS = "parent";
	private static final String ROOT_ALIAS = "root";
	private Marshaller marshaller;
	private NumberSearchCriterion criterion;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void marshalls() {
		assertEquals("Should have marshalled correctly", MARSHALLED_PROJECTION,
				marshaller.marshall(criterion));
	}

	/**
	 * Sets up test
	 */
	@Before
	public void setup() {
		criterion = new NumberSearchCriterion(PROPERTY, null, ROOT_ALIAS, FROM, TO);
		criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
		marshaller = new JacksonMarshaller(null, null);
	}

	/**
	 * alias is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoAliasSupplied() {
		new NumberSearchCriterion(PROPERTY, null, null, FROM, TO);
	}

	/**
	 * from or to are required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoFromOrToSupplied() {
		new NumberSearchCriterion(PROPERTY, null, ROOT_ALIAS, null, null);
	}

	/**
	 * property is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoPropertySupplied() {
		new NumberSearchCriterion(null, null, ROOT_ALIAS, FROM, TO);
	}

	/**
	 * Tests restriction is correct when from only is set
	 */
	@Test
	public void testRestrictionFromSet() {
		NumberSearchCriterion localCriterion = new NumberSearchCriterion(PROPERTY, null,
				ROOT_ALIAS, FROM, null);

		localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " >= ? ",
				localCriterion.getRestriction());

		localCriterion.setBooleanNot(true);
		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " < ? ",
				localCriterion.getRestriction());
	}

	/**
	 * Tests restriction is correct
	 */
	@Test
	public void testRestrictionFromToSet() {
		assertEquals("Incorrect restriction",
				NameUtils.concat(PARENT_ALIAS, PROPERTY) + " between ? and ? ",
				criterion.getRestriction());
		criterion.setBooleanNot(true);
		assertEquals("Incorrect restriction",
				NameUtils.concat(PARENT_ALIAS, PROPERTY) + " not between ? and ? ",
				criterion.getRestriction());
	}

	/**
	 * Tests restriction is correct when from and to are equal
	 */
	@Test
	public void testRestrictionFromToSetEqual() {
		NumberSearchCriterion localCriterion = new NumberSearchCriterion(PROPERTY, null,
				ROOT_ALIAS, TO, TO);

		localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Wrong number of params", 1, localCriterion.getValues().size());
		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " = ? ",
				localCriterion.getRestriction());

		localCriterion.setBooleanNot(true);
		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " != ? ",
				localCriterion.getRestriction());
	}

	/**
	 * Tests restriction is correct when to only is set
	 */
	@Test
	public void testRestrictionToSet() {
		NumberSearchCriterion localCriterion = new NumberSearchCriterion(PROPERTY, null,
				ROOT_ALIAS, null, TO);

		localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " <= ? ",
				localCriterion.getRestriction());

		localCriterion.setBooleanNot(true);
		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " > ? ",
				localCriterion.getRestriction());
	}

	/**
	 * Test values returned are as expected when only from set
	 */
	@Test
	public void testValuesFromSet() {
		NumberSearchCriterion localCriterion = new NumberSearchCriterion(PROPERTY, null,
				ROOT_ALIAS, FROM, null);
		List<Object> values = localCriterion.getValues();

		assertNotNull("values cannot be null", values);
		assertEquals("Incorrect number of values", 1, values.size());
		assertEquals("Incorrect value", FROM, values.get(0));
	}

	/**
	 * Test values returned are as expected
	 */
	@Test
	public void testValuesFromToSet() {
		List<Object> values = criterion.getValues();

		assertNotNull("values cannot be null", values);
		assertEquals("Incorrect number of values", 2, values.size());
		assertEquals("Incorrect value", FROM, values.get(0));
		assertEquals("Incorrect value", TO, values.get(1));
	}

	/**
	 * Test values returned are as expected when only to set
	 */
	@Test
	public void testValuesToSet() {
		NumberSearchCriterion localCriterion = new NumberSearchCriterion(PROPERTY, null,
				ROOT_ALIAS, null, TO);
		List<Object> values = localCriterion.getValues();

		assertNotNull("values cannot be null", values);
		assertEquals("Incorrect number of values", 1, values.size());
		assertEquals("Incorrect value", TO, values.get(0));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void unmarshalls() {
		NumberSearchCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
				NumberSearchCriterion.class);

		assertNotNull("Should have returned projection", unmarshalledCrit);
		assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
		assertEquals("Should have set from", FROM, unmarshalledCrit.getFrom());
		assertEquals("Should have set to", TO, unmarshalledCrit.getTo());
		assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
	}
}
