package org.cucina.engine.server.definition;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class CheckDescriptorDto
		implements ProcessElementDto {
	private HashMap<String, Object> properties = new HashMap<String, Object>();
	private Object domainId;
	private String application;
	private String domainType;
	private String path;

	/**
	 * @return .
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * @param application .
	 */
	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * @return .
	 */
	public Object getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId .
	 */
	public void setDomainId(Object domainId) {
		this.domainId = domainId;
	}

	/**
	 * @return .
	 */
	public String getDomainType() {
		return domainType;
	}

	/**
	 * @param domainType .
	 */
	public void setDomainType(String domainType) {
		this.domainType = domainType;
	}

	/**
	 * @return .
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path .
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get properties
	 *
	 * @return HashMap<String ,   Object>
	 */
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Set properties
	 *
	 * @param properties HashMap<String, Object>
	 */
	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
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
