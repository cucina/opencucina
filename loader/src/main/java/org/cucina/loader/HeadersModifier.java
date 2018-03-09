package org.cucina.loader;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface HeadersModifier {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param headers JAVADOC.
	 * @param clazz   JAVADOC.
	 * @return JAVADOC.
	 */
	String[] modifyHeaders(String[] headers, Class<?> clazz);
}
