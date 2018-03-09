package org.cucina.engine.server.definition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashMap;


/**
 * Describes a workflow component
 *
 * @author vlevine
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "classType")
public interface ProcessElementDto {
	/**
	 * @return .
	 */
	String getApplication();

	/**
	 * @return .
	 */
	Object getDomainId();

	/**
	 * @return .
	 */
	String getDomainType();

	/**
	 * @return .
	 */
	String getPath();

	/**
	 * @return .
	 */
	HashMap<String, Object> getProperties();
}
