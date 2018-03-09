package org.cucina.search.query.criterion;

import org.cucina.core.marshal.JacksonMarshaller;
import org.cucina.core.marshal.Marshaller;
import org.cucina.search.query.Join;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Tests JoinCriterion functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class JoinCriterionTest {
	private static final String MARSHALLED_PROJECTION = "{\"@class\":\"org.cucina.search.query.criterion.JoinCriterion\",\"name\":\"default\",\"rootAlias\":\"root\",\"alias\":\"default\",\"booleanNot\":false,\"lhs\":\"foo.id\",\"lhsRootAlias\":\"lhsAlias\",\"rhs\":\"foo\",\"rhsRootAlias\":\"rhsAlias\"}";
	private static final String RHS = "foo";
	private static final String LHS = "foo.id";
	private static final String ROOT_ALIAS = "root";
	private static final String RHS_ALIAS = "rhsAlias";
	private static final String LHS_ALIAS = "lhsAlias";
	private JoinCriterion criterion;
	private Marshaller marshaller;

	/**
	 * Test values returned are as expected
	 */
	@Test
	public void getJoins() {
		Map<String, List<Join>> joins = criterion.getJoins();

		assertNotNull("joins cannot be null", joins);
		assertEquals("Incorrect number of joins", 2, joins.size());

		List<Join> lhsJoins = joins.get(LHS_ALIAS);

		assertEquals("Incorrect number joins", 1, lhsJoins.size());
		assertEquals("incorrect join", "foo", lhsJoins.get(0).getProperty());

		List<Join> rhsJoins = joins.get(RHS_ALIAS);

		assertEquals("Incorrect number joins", 0, rhsJoins.size());
	}

	/**
	 * Tests marshalls values correctly
	 */
	@Test
	public void marshalls() {
		System.out.println(marshaller.marshall(criterion));
		assertEquals("Should have marshalled correctly", MARSHALLED_PROJECTION,
				marshaller.marshall(criterion));
	}

	/**
	 * Sets up test
	 */
	@Before
	public void setup() {
		criterion = new JoinCriterion(ROOT_ALIAS, LHS, LHS_ALIAS, RHS, RHS_ALIAS);
		marshaller = new JacksonMarshaller(null, null);
	}

	/**
	 * Tests restriction is correct
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoAlias() {
		Map<String, String> parentAliases = new HashMap<String, String>();

		parentAliases.put(RHS_ALIAS, "fred");
		criterion.setParentAliases(parentAliases);
		criterion.getRestriction();
	}

	/**
	 * alias is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoLHSSupplied() {
		new TextSearchCriterion(ROOT_ALIAS, null, null, RHS);
	}

	/**
	 * alias is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoRHSSupplied() {
		new TextSearchCriterion(ROOT_ALIAS, null, LHS, null);
	}

	/**
	 * alias is required
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNoRootAliasSupplied() {
		new TextSearchCriterion(null, null, LHS, RHS);
	}

	/**
	 * Tests restriction is correct
	 */
	@Test
	public void testRestriction() {
		Map<String, String> parentAliases = new HashMap<String, String>();

		parentAliases.put(RHS_ALIAS, "fred");
		parentAliases.put("foo", "bob");
		criterion.setParentAliases(parentAliases);

		assertEquals("Incorrect restriction", "bob.id" + " = " + "fred.foo",
				criterion.getRestriction());
	}

	/**
	 * Tests restriction is correct
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRestrictionNoAliases() {
		criterion.getRestriction();
	}

	/**
	 * Test values returned are as expected
	 */
	@Test
	public void testValues() {
		List<Object> values = criterion.getValues();

		assertNotNull("values cannot be null", values);
		assertEquals("Incorrect number of values", 0, values.size());
	}

	/**
	 * unmarshalls values correctly
	 */
	@Test
	public void unmarshalls() {
		JoinCriterion unmarshalledCrit = marshaller.unmarshall(MARSHALLED_PROJECTION,
				JoinCriterion.class);

		assertNotNull("Should have returned projection", unmarshalledCrit);
		assertEquals("Shouldn't have set name", "default", unmarshalledCrit.getName());
		assertEquals("Should have set rhs", RHS, unmarshalledCrit.getRhs());
		assertEquals("Should have set lhs", LHS, unmarshalledCrit.getLhs());
		assertEquals("Should have set rhs alias", RHS_ALIAS, unmarshalledCrit.getRhsRootAlias());
		assertEquals("Should have set lhs alias", LHS_ALIAS, unmarshalledCrit.getLhsRootAlias());
		assertEquals("Should have set rootAlias", ROOT_ALIAS, unmarshalledCrit.getRootAlias());
	}
}
