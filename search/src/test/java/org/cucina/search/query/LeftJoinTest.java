package org.cucina.search.query;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Test LeftJoin functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class LeftJoinTest {
	private static final String PROPERTY = "prop";
	private LeftJoin leftJoin;

	/**
	 * Set up for test
	 */
	@Before
	public void setup() {
		leftJoin = new LeftJoin(PROPERTY, false);
	}

	/**
	 * test join is correct
	 */
	@Test
	public void testJoin() {
		assertEquals("leftJoin not as expected", "left join ", leftJoin.getJoin());
	}

	/**
	 * Test property value returns correctly
	 */
	@Test
	public void testProperty() {
		assertEquals("property not as expected", PROPERTY, leftJoin.getProperty());
	}

	/**
	 * Test is unique
	 */
	@Test
	public void testUnique() {
		assertFalse("unique not as expected", leftJoin.isUnique());
		leftJoin = new LeftJoin(PROPERTY, true);
		assertTrue("unique not as expected", leftJoin.isUnique());
	}
}
