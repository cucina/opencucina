package org.cucina.loader;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class LoaderClassDescriptorTest {
	/**
	 * JAVADOC Method Level Comments
	 */

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testLoaderColumns() {
		assertTrue(LoaderClassDescriptor.isLoaderColumns(Annoited.class));
		assertFalse(LoaderClassDescriptor.isLoaderColumns(NonAnnoited.class));
		assertNull(LoaderClassDescriptor.getLoaderColumns(NonAnnoited.class));

		List<String> ret = LoaderClassDescriptor.getLoaderColumns(Annoited.class);

		assertEquals(3, ret.size());
		assertTrue(ret.contains("name"));
		assertTrue(ret.contains("number"));
		assertTrue(ret.contains("strings"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testLookup() {
		Map<String, String> lookups = LoaderClassDescriptor.getLoaderColumnLookup(Annoited.class);

		assertTrue(lookups.size() == 2);
		assertTrue(lookups.keySet().contains("testNAME"));
		assertTrue(lookups.keySet().contains("anotherName"));
		assertEquals("value", lookups.get("testNAME"));
		assertEquals("value", lookups.get("anotherName"));
	}

	/**
	 * JAVADOC for Class Level
	 *
	 * @author $Author: $
	 * @version $Revision: $
	 */
	@LoaderColumns
	public class Annoited {
		private Collection<String> strings;
		private String name;
		@LoaderColumnLookup(propertyAlias = {
				"testNAME", "anotherName"}
		)
		private double value;
		private int number;

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @return JAVADOC.
		 */
		public String getName() {
			return name;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @param name JAVADOC.
		 */
		@LoaderColumn
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @return JAVADOC.
		 */
		public int getNumber() {
			return number;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @param number JAVADOC.
		 */
		@LoaderColumn
		public void setNumber(int number) {
			this.number = number;
		}

		public Collection<String> getStrings() {
			return strings;
		}

		@LoaderColumn
		public void setStrings(Collection<String> strings) {
			this.strings = strings;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @return JAVADOC.
		 */
		public double getValue() {
			return value;
		}

		/**
		 * JAVADOC Method Level Comments
		 *
		 * @param value JAVADOC.
		 */
		public void setValue(double value) {
			this.value = value;
		}
	}

	public class NonAnnoited {
	}
}
