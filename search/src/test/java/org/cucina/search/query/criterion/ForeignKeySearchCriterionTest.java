package org.cucina.search.query.criterion;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.testassist.Foo;
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
public class ForeignKeySearchCriterionTest {
	private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.ForeignKeySearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"value\":[1]}";
	private static final String MULTI_MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.ForeignKeySearchCriterion\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"name\",\"booleanNot\":false,\"value\":[1,2,3]}";
	private static final String ROOT_ALIAS = "root";
	private static final String PROPERTY = "name";
	private static final String PARENT_ALIAS = "parent";
	private ForeignKeySearchCriterion criterion;
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
						new ForeignKeySearchCriterion(PROPERTY, null, ROOT_ALIAS, Arrays.asList(1L, 2L, 3L))));
	}

	/**
	 * Sets up test
	 */
	@Before
	public void setup() {
		criterion = new ForeignKeySearchCriterion(PROPERTY, null, ROOT_ALIAS, "1");
		criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
		marshaller = new JacksonMarshaller(null, null);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testMoreThanOne() {
		criterion = new ForeignKeySearchCriterion(PROPERTY, null, ROOT_ALIAS,
				Arrays.asList(1L, 2L, 3L));
		criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
		assertEquals("Should return in with multiple values", PARENT_ALIAS + ".name.id in (?,?,?)",
				criterion.getRestriction());
	}

	/**
	 * alias is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoAliasSupplied() {
		new ForeignKeySearchCriterion(PROPERTY, null, null, "1");
	}

	/**
	 * match is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoMatchSupplied() {
		new ForeignKeySearchCriterion(PROPERTY, null, ROOT_ALIAS, null);
	}

	/**
	 * property is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoPropertySupplied() {
		new ForeignKeySearchCriterion(null, null, ROOT_ALIAS, "1");
	}

	/**
	 * Tests restriction is correct
	 */
	@Test
	public void testRestriction() {
		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + ".id = ?",
				criterion.getRestriction());
	}

	/**
	 * Tests restriction is correct
	 */
	@Test
	public void testRestrictionLong() {
		criterion = new ForeignKeySearchCriterion(PROPERTY, null, ROOT_ALIAS, 1L);
		criterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));
		assertEquals("Incorrect restriction", NameUtils.concat(PARENT_ALIAS, PROPERTY) + ".id = ?",
				criterion.getRestriction());
	}

	/**
	 * Tests restriction is correct
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRestrictionNotSupported() {
		criterion = new ForeignKeySearchCriterion(PROPERTY, null, ROOT_ALIAS, new Foo());
	}

	/**
	 * Tests property is nested two levels, should return final nested level as alias and
	 * property
	 */
	@Test
	public void testSearchPropertyNameNestedTwoLevels() {
		ForeignKeySearchCriterion localCriterion = new ForeignKeySearchCriterion(
				"grandParent.parent." + PROPERTY, null, ROOT_ALIAS, "1");

		localCriterion.setParentAliases(Collections.singletonMap("parent", PARENT_ALIAS));

		assertEquals("Should return just root", NameUtils.concat("parent", PROPERTY) + ".id = ?",
				localCriterion.getRestriction());
	}

	/**
	 * Tests property is at root level
	 */
	@Test
	public void testSearchPropertyNameRootLevel() {
		ForeignKeySearchCriterion localCriterion = new ForeignKeySearchCriterion(PROPERTY, null,
				ROOT_ALIAS, "1");

		localCriterion.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Should return just root",
				NameUtils.concat(PARENT_ALIAS, PROPERTY) + ".id = ?", localCriterion.getRestriction());
	}

	/**
	 * Tests property is nested one level, should return property provided
	 */
	@Test
	public void testSearchPropertyNestedOneLevels() {
		ForeignKeySearchCriterion localCriterion = new ForeignKeySearchCriterion("parent." +
				PROPERTY, null, ROOT_ALIAS, "1");

		localCriterion.setParentAliases(Collections.singletonMap("parent", PARENT_ALIAS));

		assertEquals("Should return just root",
				NameUtils.concat(PARENT_ALIAS, PROPERTY) + ".id = ?", localCriterion.getRestriction());
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
		ForeignKeySearchCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
				ForeignKeySearchCriterion.class);

		assertNotNull("Should have returned projection", unmarshalledCrit);
		assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
		assertTrue("Should have set foreign id",
				unmarshalledCrit.getValue().containsAll(Collections.singleton(1L)));
		assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());

		unmarshalledCrit = marshaller.unmarshall(MULTI_MARSHALLED_PROJECTION,
				ForeignKeySearchCriterion.class);

		assertNotNull("Should have returned projection", unmarshalledCrit);
		assertEquals("Should have set name", PROPERTY, unmarshalledCrit.getName());
		assertTrue("Should have set foreign id",
				unmarshalledCrit.getValue().containsAll(Arrays.asList(1L, 2L, 3L)));
		assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
	}
}
