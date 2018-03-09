package org.cucina.core.model.support;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@Converter
public class BooleanConverter
		implements AttributeConverter<Boolean, String> {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param value JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public String convertToDatabaseColumn(Boolean value) {
		if (value) {
			return "Y";
		}

		return "N";
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param value JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public Boolean convertToEntityAttribute(String value) {
		return "Y".equals(value);
	}
}
