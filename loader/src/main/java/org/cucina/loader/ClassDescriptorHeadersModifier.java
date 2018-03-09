package org.cucina.loader;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class ClassDescriptorHeadersModifier
		implements HeadersModifier {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param headers JAVADOC.
	 * @param clazz   JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public String[] modifyHeaders(String[] headers, Class<?> clazz) {
		Map<String, String> lookups = LoaderClassDescriptor.getLoaderColumnLookup(clazz);
		String[] ret = new String[headers.length];

		for (int i = 0; i < headers.length; i++) {
			ret[i] = headers[i];

			String lookup = lookups.get(headers[i]);

			if (StringUtils.isNotBlank(lookup)) {
				ret[i] = lookup;
			}
		}

		return ret;
	}
}
