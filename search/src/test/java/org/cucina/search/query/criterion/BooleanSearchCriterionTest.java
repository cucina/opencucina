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
 * Tests BooleanSearchCriterion functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class BooleanSearchCriterionTest {
	private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.BooleanSearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"value\":true}";
	private static final String PROPERTY = "name";
	private static final boolean VALUE = true;
	private static final String ROOT_ALIAS = "root";
	private static final String PARENT_ALIAS = "parent";
	private BooleanSearchCriterion criterion;
	private Marshaller marshaller;

	/**
	 * Tests marshalls values correctly
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
		criterion = new BooleanSearchCriterion(PROPERTY, null, ROOT_ALIAS, VALUE);
		criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
		marshaller = new JacksonMarshaller(null, null);
	}

	/**
	 * alias is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoAliasSupplied() {
		new BooleanSearchCriterion(PROPERTY, null, null, VALUE);
	}

	/**
	 * property is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoPropertySupplied() {
		new BooleanSearchCriterion(null, null, ROOT_ALIAS, VALUE);
	}

	/**
	 * Tests restriction is correct
	 */
	@Test
	public void testRestriction() {
		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " = ?",
				criterion.getRestriction());
	}

	/**
	 * Ok, this one applies to an object rather than String
	 */
	@Test
	public void testSearchPropertyNameIsAlias() {
		BooleanSearchCriterion localCriterion = new BooleanSearchCriterion(PARENT_ALIAS, null,
				ROOT_ALIAS, VALUE);

		localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Should return just parent", PARENT_ALIAS + " = ?",
				localCriterion.getRestriction());
	}

	/**
	 * Tests property is at root level
	 */
	@Test
	public void testSearchPropertyNameRootLevel() {
		BooleanSearchCriterion localCriterion = new BooleanSearchCriterion(PROPERTY, null,
				ROOT_ALIAS, VALUE);

		localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Should return just root", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " = ?",
				localCriterion.getRestriction());
	}

	/**
	 * Test values returned are as expected
	 */
	@Test
	public void testValues() {
		List<Object> values = criterion.getValues();

		assertNotNull("values cannot be null", values);
		assertEquals("Incorrect number of values", 1, values.size());
		assertEquals("Incorrect value", VALUE, values.get(0));
	}

	/**
	 * unmarshalls values correctly
	 */
	@Test
	public void unmarshalls() {
		BooleanSearchCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
				BooleanSearchCriterion.class);

		assertNotNull("Should have returned projection", unmarshalledCrit);
		assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
		assertEquals("Should have set text", VALUE, unmarshalledCrit.getValue());
		assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
	}
}
