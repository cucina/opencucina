package org.cucina.loader;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CsvlineWrapper {
	private String applicationType;
	private String[] headers;
	private String[] line;

	/**
	 * Creates a new CsvlineWrapper object.
	 *
	 * @param line            JAVADOC.
	 * @param headers         JAVADOC.
	 * @param applicationType JAVADOC.
	 */
	public CsvlineWrapper(String[] line, String[] headers, String applicationType) {
		this.line = line;
		this.headers = headers;
		this.applicationType = applicationType;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getApplicationType() {
		return applicationType;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String[] getLine() {
		return line;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
