package org.cucina.core.model.support;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvCollectionConverterTest {
	private CsvCollectionConverter converter;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @throws Exception JAVADOC.
	 */
	@Before
	public void setUp()
			throws Exception {
		MockitoAnnotations.initMocks(this);
		converter = new CsvCollectionConverter();
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testConvertDataValueToObjectValue() {
		Object result = converter.convertToEntityAttribute("a, b,c");

		assertTrue("Not an instanceof Collection", result instanceof Collection<?>);

		@SuppressWarnings("unchecked")
		Collection<String> sr = (Collection<String>) result;

		assertEquals(3, sr.size());
		assertTrue("No a", sr.contains("a"));
		assertTrue("No b", sr.contains("b"));
		assertTrue("No c", sr.contains("c"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testConvertDataValueToObjectValueNull() {
		assertNull("Shouldn't have converted anything", converter.convertToEntityAttribute(null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testConvertObjectValueToDataValue() {
		Collection<String> coll = new ArrayList<String>();

		coll.add("a");
		coll.add("b");
		coll.add("a");

		Object result = converter.convertToDatabaseColumn(coll);

		assertNotNull("result is null", result);

		String sr = (String) result;

		assertEquals("a,b,a", sr);
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testConvertObjectValueToDataValueNull() {
		assertNull("Shouldn't have converted anything", converter.convertToDatabaseColumn(null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void toStringNull() {
		assertNull(CsvCollectionConverter.toString(null));
	}
}
