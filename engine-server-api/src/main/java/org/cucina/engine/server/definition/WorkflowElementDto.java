package org.cucina.engine.server.definition;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * Describes a workflow component
 *
 * @author vlevine
  */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "classType")
public interface WorkflowElementDto {
    /**
     *
     *
     * @return .
     */
    String getApplication();

    /**
     *
     *
     * @return .
     */
    Object getDomainId();

    /**
     *
     *
     * @return .
     */
    String getDomainType();

    /**
     *
     *
     * @return .
     */
    String getPath();

    /**
     *
     *
     * @return .
     */
    HashMap<String, Object> getProperties();
}
