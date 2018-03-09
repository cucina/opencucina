package org.cucina.search.query.criterion;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.core.utils.NameUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class InSearchCriterionTest {
	private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.InSearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"value\":[\"a\"]}";
	private static final String MULTI_MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.InSearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"value\":[\"a\",\"b\",\"c\"]}";
	private static final String ROOT_ALIAS = "root";
	private static final String PARENT_ALIAS = "parent";
	private static final String PROPERTY = "name";
	private InSearchCriterion criterion;
	private Marshaller marshaller;

	/**
	 * Tests marshalls values correctly
	 */
	@Test
	public void marshalls() {
		assertEquals("Should have marshalled correctly", MARSHALLED_PROJECTION,
				marshaller.marshall(criterion));

		assertEquals("Should have marshalled correctly", MULTI_MARSHALLED_PROJECTION,
				marshaller.marshall(
						new InSearchCriterion(PROPERTY, null, ROOT_ALIAS, Arrays.asList("a", "b", "c"))));
	}

	/**
	 * Sets up test
	 */
	@Before
	public void setup() {
		criterion = new InSearchCriterion(PROPERTY, null, ROOT_ALIAS, Collections.singleton("a"));
		criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
		marshaller = new JacksonMarshaller(null, null);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testMoreThanOne() {
		criterion = new InSearchCriterion(PROPERTY, null, ROOT_ALIAS, Arrays.asList("a", "b", "c"));
		criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
		assertEquals("Should return in with multiple values", PARENT_ALIAS + ".name in (?,?,?)",
				criterion.getRestriction());
	}

	/**
	 * alias is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoAliasSupplied() {
		new InSearchCriterion(PROPERTY, null, null, Collections.singleton("a"));
	}

	/**
	 * match is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoMatchSupplied() {
		new InSearchCriterion(PROPERTY, null, ROOT_ALIAS, null);
	}

	/**
	 * property is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoPropertySupplied() {
		new InSearchCriterion(null, null, ROOT_ALIAS, Collections.singleton("a"));
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
	 * Tests property is nested two levels, should return final nested level as alias and
	 * property
	 */
	@Test
	public void testSearchPropertyNameNestedTwoLevels() {
		InSearchCriterion localCriterion = new InSearchCriterion("grandParent.parent." + PROPERTY,
				null, ROOT_ALIAS, Collections.singleton("a"));

		localCriterion.setParentAliases(Collections.singletonMap("parent", PARENT_ALIAS));

		assertEquals("Should return just root", NameUtils.concat("parent", PROPERTY) + " = ?",
				localCriterion.getRestriction());
	}

	/**
	 * Tests property is at root level
	 */
	@Test
	public void testSearchPropertyNameRootLevel() {
		InSearchCriterion localCriterion = new InSearchCriterion(PROPERTY, null, ROOT_ALIAS,
				Collections.singleton("a"));

		localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Should return just root", NameUtils.concat(PARENT_ALIAS, PROPERTY) + " = ?",
				localCriterion.getRestriction());
	}

	/**
	 * Tests property is nested one level, should return property provided
	 */
	@Test
	public void testSearchPropertyNestedOneLevels() {
		InSearchCriterion localCriterion = new InSearchCriterion("parent." + PROPERTY, null,
				ROOT_ALIAS, Collections.singleton("a"));

		localCriterion.setParentAliases(Collections.singletonMap("parent", PARENT_ALIAS));

		assertEquals("Should return just root", NameUtils.concat("parent", PROPERTY) + " = ?",
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
	}

	/**
	 * unmarshalls values correctly
	 */
	@Test
	public void unmarshalls() {
		InSearchCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
				InSearchCriterion.class);

		assertNotNull("Should have returned projection", unmarshalledCrit);
		assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
		assertTrue("Should have set foreign id",
				unmarshalledCrit.getValues().containsAll(Collections.singleton("a")));
		assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());

		unmarshalledCrit = marshaller.unmarshall(MULTI_MARSHALLED_PROJECTION,
				InSearchCriterion.class);

		assertNotNull("Should have returned projection", unmarshalledCrit);
		assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
		assertTrue("Should have set foreign id",
				unmarshalledCrit.getValues().containsAll(Arrays.asList("a", "b", "c")));
		assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
	}
}
