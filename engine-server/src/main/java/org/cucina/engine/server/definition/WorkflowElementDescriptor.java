
package org.cucina.engine.server.definition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


/**
 * Describes a workflow component
 *
 * @author $Author: $
 * @version $Revision: $
  */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "classType")
public interface WorkflowElementDescriptor {
    /**
     * Application where the component lives
     *
     * @return JAVADOC.
     */
    String getApplication();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    Object getDomainId();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    String getDomainType();

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    String getPath();
}
