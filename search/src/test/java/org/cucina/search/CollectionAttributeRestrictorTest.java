package org.cucina.search;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CollectionAttributeRestrictorTest {
	private CollectionAttributeRestrictor restrictor;

	/**
	 * JAVADOC Method Level Comments
	 */
	@Before
	public void setup() {
		restrictor = new CollectionAttributeRestrictor();
	}

	/**
	 * Test that we do a contains any style check for <code>Collection</code> restriction
	 * values.
	 */
	@Test
	public void testCollectionValue() {
		Collection<String> attribute = Arrays.asList("A", "B", "C");
		Collection<String> restriction = Arrays.asList("D", "E", "B");

		assertTrue(restrictor.test(attribute, restriction));
	}

	/**
	 * Test that we failfast if restriction is not provided
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullAttribute() {
		restrictor.test("A", null);
	}

	/**
	 * Test that we failfast if restriction is not provided
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNullRestriction() {
		restrictor.test("A", null);
	}

	/**
	 * Test that we check a single value restriction by checking it's existence in the
	 * attribute value <code>Collection</code>
	 */
	public void testSingleValue() {
		Collection<String> attribute = Arrays.asList("A", "B", "C");

		assertTrue(restrictor.test(attribute, "C"));
	}

	/**
	 * Test that we support any <code>Collection</code>.
	 */
	@Test
	public void testSupports() {
		assertTrue(restrictor.supports(HashSet.class));
		assertTrue(restrictor.supports(ArrayList.class));
		assertFalse(restrictor.supports(String.class));
	}

	/**
	 * Test that we failfast if attribute not a <code>Collection</code>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWrongAttributeType() {
		restrictor.test("A", "");
	}
}
