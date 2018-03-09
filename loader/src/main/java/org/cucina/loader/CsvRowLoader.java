package org.cucina.loader;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;


/**
 * JAVADOC Interface Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public interface CsvRowLoader {
	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param applicationType JAVADOC.
	 * @param headers         JAVADOC.
	 * @param line            JAVADOC.
	 * @param lineNumber      JAVADOC.
	 * @throws BindException JAVADOC.
	 */
	@Transactional(rollbackFor = BindException.class)
	void processRow(String applicationType, String[] headers, String[] line, int lineNumber)
			throws BindException;
}
