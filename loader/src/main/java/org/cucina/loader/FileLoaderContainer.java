package org.cucina.loader;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.UUID;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class FileLoaderContainer
		implements Serializable {
	private static final long serialVersionUID = -3210160235797083727L;
	private String applicationType;
	private UUID uuid;
	private String[] data;
	private String[] headers;
	private int lineNumber;

	/**
	 * Creates a new FileLoaderContainer object.
	 */
	public FileLoaderContainer() {
	}

	/**
	 * Creates a new FileLoaderContainer object.
	 *
	 * @param applicationType JAVADOC.
	 * @param headers         JAVADOC.
	 * @param data            JAVADOC.
	 * @param lineNumber      JAVADOC.
	 */
	public FileLoaderContainer(String applicationType, String[] headers, String[] data,
							   int lineNumber) {
		this.applicationType = applicationType;
		this.headers = headers;
		this.data = data;
		this.lineNumber = lineNumber;
	}

	/**
	 * Creates a new FileLoaderContainer object.
	 *
	 * @param headers JAVADOC.
	 * @param data    JAVADOC.
	 */
	public FileLoaderContainer(String applicationType, String[] headers, String[] data) {
		this.applicationType = applicationType;
		this.headers = headers;
		this.data = data;
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
	 * @param applicationType JAVADOC.
	 */
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String[] getData() {
		return data;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param data JAVADOC.
	 */
	public void setData(String[] data) {
		this.data = data;
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
	 * @param headers JAVADOC.
	 */
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param lineNumber JAVADOC.
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param uuid JAVADOC.
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * Default toString implementation
	 *
	 * @return This object as String.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
