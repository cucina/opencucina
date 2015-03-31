package org.cucina.security.api;

import java.io.Serializable;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class PermissionDto
    implements Serializable {
    private static final long serialVersionUID = 2532515604491906815L;
    private Collection<DimensionDto> dimensions;
    private String name;

    /**
    * JAVADOC Method Level Comments
    *
    * @param dimensions JAVADOC.
    */
    public void setDimensions(Collection<DimensionDto> dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Collection<DimensionDto> getDimensions() {
        return dimensions;
    }

    /**
     *
     *
     * @param name .
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     *
     * @return .
     */
    public String getName() {
        return name;
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
