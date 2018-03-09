package org.cucina.search.query.projection;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.core.utils.NameUtils;
import org.cucina.search.query.Join;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Test SimplePropertyProjection functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SimplePropertyProjectionTest {
	private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.projection.SimplePropertyProjection\",\"name\":\"name\",\"rootAlias\":\"root\",\"alias\":\"myName\"}";
	private static final String PROPERTY = "name";
	private static final String ALIAS = "myName";
	private static final String ROOT_ALIAS = "root";
	private static final String PARENT_ALIAS = "parent";
	private Marshaller marshaller;
	private SimplePropertyProjection projection;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void getJoins() {
		SimplePropertyProjection projection = new SimplePropertyProjection("bars.bazs." + PROPERTY,
				ALIAS, ROOT_ALIAS);

		Map<String, List<Join>> distinctJoins = projection.getJoins();

		assertNotNull(distinctJoins);
		assertEquals("Incorrect number of distinct joins", 1, distinctJoins.size());

		List<Join> joins = distinctJoins.get(ROOT_ALIAS);

		assertEquals("Incorrect number of joins", 2, joins.size());

		Join join = joins.get(0);

		assertEquals("Incorrect property", "bars", join.getProperty());
		assertFalse("Should not be unique", join.isUnique());
		join = joins.get(1);
		assertEquals("Incorrect property", "bazs", join.getProperty());
		assertFalse("Should not be unique", join.isUnique());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void marshalls() {
		assertEquals("Should have marshalled correctly", MARSHALLED_PROJECTION,
				marshaller.marshall(projection));
	}

	/**
	 * Set up as expected.
	 */
	@Before
	public void onSetup() {
		projection = new SimplePropertyProjection(PROPERTY, ALIAS, ROOT_ALIAS);
		marshaller = new JacksonMarshaller(null, null);
	}

	/**
	 * Test requires property
	 */
	@Test(expected = IllegalArgumentException.class)
	public void requiresProperty() {
		new SimplePropertyProjection(null, ALIAS, ROOT_ALIAS);
	}

	/**
	 * Tests that concats parentAlias and property name with as alias.
	 */
	@Test
	public void testProjection() {
		SimplePropertyProjection localProjection = new SimplePropertyProjection(PROPERTY, ALIAS,
				ROOT_ALIAS);

		localProjection.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Should return just root",
				NameUtils.concat(PARENT_ALIAS, PROPERTY) + " as " + ALIAS,
				localProjection.getProjection());
	}

	/**
	 * Tests that concats parentAlias and final property name with as alias.
	 */
	@Test
	public void testProjectionNested() {
		SimplePropertyProjection localProjection = new SimplePropertyProjection(
				"grandParent.parent." + PROPERTY, ALIAS, ROOT_ALIAS);

		localProjection.setParentAliases(Collections.singletonMap("parent", PARENT_ALIAS));

		assertEquals("Should return just root",
				NameUtils.concat(PARENT_ALIAS, PROPERTY) + " as " + ALIAS,
				localProjection.getProjection());
	}

	/**
	 * If no alias provided uses propertyName
	 */
	@Test
	public void testProjectionNoAlias() {
		SimplePropertyProjection localProjection = new SimplePropertyProjection(PROPERTY, null,
				ROOT_ALIAS);

		localProjection.setParentAliases(Collections.singletonMap(ROOT_ALIAS, PARENT_ALIAS));

		assertEquals("Incorrect projection",
				NameUtils.concat(PARENT_ALIAS, PROPERTY) + " as " + PROPERTY,
				localProjection.getProjection());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void unmarshalls() {
		SimplePropertyProjection unmarshalledProj = marshaller.unmarshall(MARSHALLED_PROJECTION,
				SimplePropertyProjection.class);

		assertNotNull("Should have returned projection", unmarshalledProj);
		assertEquals("Should have set name", PROPERTY, unmarshalledProj.getName());
		assertEquals("Should have set name", ALIAS, unmarshalledProj.getAlias());
		assertEquals("Should have set name", ROOT_ALIAS, unmarshalledProj.getRootAlias());
	}
}
