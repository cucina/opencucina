package org.cucina.core.model.support;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvMapConverterTest {
	private CsvMapConverter handler = new CsvMapConverter();

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToDatasetValue()
			throws Exception {
		Map<String, String> bean = new LinkedHashMap<String, String>();

		bean.put("a", "1");
		bean.put("b", "2");

		assertEquals("Incorrect value", "a=1,b=2", handler.convertToDatabaseColumn(bean));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToDatasetValueEmpty()
			throws Exception {
		assertNull("Incorrect value",
				handler.convertToDatabaseColumn(new HashMap<String, String>()));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToDatasetValueNull()
			throws Exception {
		assertNull("Incorrect value", handler.convertToDatabaseColumn(null));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToObjectValue()
			throws Exception {
		Map<String, String> value = handler.convertToEntityAttribute("a=1,b=2");

		assertNotNull("Should have generated value", value);
		assertEquals("Key not present or incorrect mapped value", "1", value.get("a"));
		assertEquals("Key not present or incorrect mapped value", "2", value.get("b"));
	}

	/**
	 * JAVADOC Method Level Comments
	 */
	@Test
	public void testToObjectValueNull()
			throws Exception {
		assertNull("Shouldn't have generated value",
				handler.convertToEntityAttribute(null));
	}
}
