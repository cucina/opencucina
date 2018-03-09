package org.cucina.loader.processor;

/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface Processor {

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param object JAVADOC.
	 * @return collection of ids of newly created objects if any or null.
	 */
	void process(Object object);
}
