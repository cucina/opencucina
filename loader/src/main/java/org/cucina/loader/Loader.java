package org.cucina.loader;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface Loader {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationType JAVADOC.
	 * @param attachment      JAVADOC.
	 * @return
	 */
	int loadCollection(String applicationType, byte[] data)
			throws LoaderException;
}
