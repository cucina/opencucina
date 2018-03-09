package org.cucina.core;

import org.cucina.core.testassist.Foo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class PackageBasedInstanceFactoryTest {
	private PackageBasedInstanceFactory instanceFactory;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void before()
			throws Exception {
		instanceFactory = new PackageBasedInstanceFactory(Foo.class.getPackage().getName());
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void classType() {
		assertEquals("Incorrect class type", Foo.class, instanceFactory.getClassType("Foo"));
		assertNull("Made up type", instanceFactory.getClassType("MadeUpType"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetBean() {
		assertNotNull(instanceFactory.getBean("Foo"));
		assertNull(instanceFactory.getBean("String"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testGetPropertyTypeString() {
		assertEquals("String", instanceFactory.getPropertyType("Foo", "name"));
		assertNull(instanceFactory.getPropertyType("Foo", "xxx"));
	}

	/**
	 * Test nested properties
	 */
	@Test
	public void testGetPropertyTypeStringNested() {
		assertEquals("String", instanceFactory.getPropertyType("Foo", "bars.name"));
		assertNull(instanceFactory.getPropertyType("Foo", "excesses.xxx"));
	}

	/**
	 * Test Foreign keys
	 */
	@Test
	public void testIsForeignKey() {
		assertFalse("not a foreignKey", instanceFactory.isForeignKey("Foo", "name"));
		assertFalse("not a foreignKey", instanceFactory.isForeignKey("Foo", "bars"));
		assertTrue("Should be foreignKey", instanceFactory.isForeignKey("Baz", "foo"));
		assertFalse("not a foreignKey", instanceFactory.isForeignKey("Baz", "bars"));
	}
}
