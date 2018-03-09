package org.cucina.loader;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class MappedHeadersModifier
		implements HeadersModifier {
	private Map<Class<?>, Map<String, String>> headersMap;

	/**
	 * Creates a new MappedHeadersModifier object.
	 *
	 * @param headersMap JAVADOC.
	 */
	public MappedHeadersModifier(Map<Class<?>, Map<String, String>> headersMap) {
		Assert.notNull(headersMap, "headersMap is null");
		this.headersMap = headersMap;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param headers JAVADOC.
	 * @param clazz   JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public String[] modifyHeaders(String[] headers, Class<?> clazz) {
		Map<String, String> map = headersMap.get(clazz);

		if (CollectionUtils.isEmpty(map)) {
			return headers;
		}

		String[] result = new String[headers.length];

		for (int i = 0; i < headers.length; i++) {
			String replacement = map.get(headers[i]);

			result[i] = replacement;
		}

		return result;
	}
}
