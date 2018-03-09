package org.cucina.core.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * Test SearchNameUtils functions
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class NameUtilsTest {
	private static final String PROPERTY = "name";
	private static final String OWNER_PROPERTY = "owner.name";
	private static final String PARENT_PROPERTY = "parent.owner.name";

	/**
	 * Test alias retrieved correctly
	 */
	@Test
	public void getAlias() {
		assertEquals("Incorrect alias", PROPERTY, NameUtils.getAlias(PROPERTY));
		assertEquals("Incorrect alias", "owner", NameUtils.getAlias(OWNER_PROPERTY));
		assertEquals("Incorrect alias", "owner", NameUtils.getAlias(PARENT_PROPERTY));
	}

	/**
	 * Test name retrieved correctly
	 */
	@Test
	public void getName() {
		assertEquals("Incorrect name", PROPERTY, NameUtils.getName(PROPERTY));
		assertEquals("Incorrect name", PROPERTY, NameUtils.getName(OWNER_PROPERTY));
		assertEquals("Incorrect name", PROPERTY, NameUtils.getName(PARENT_PROPERTY));
	}

	/**
	 * Test type retrieved correctly
	 */
	@Test
	public void getType() {
		assertNull("No type provided", NameUtils.getType(PROPERTY));
		assertEquals("Incorrect type", "owner", NameUtils.getType(OWNER_PROPERTY));
		assertEquals("Incorrect type", "parent", NameUtils.getType(PARENT_PROPERTY));
	}

	/**
	 * Test concatenation valid
	 */
	@Test
	public void concat() {
		assertEquals("Incorrect name", OWNER_PROPERTY, NameUtils.concat("owner", PROPERTY));
	}

	/**
	 * Test concatenation valid
	 */
	@Test
	public void concatNoBase() {
		assertEquals("Incorrect name", PROPERTY, NameUtils.concat(null, PROPERTY));
	}
}
