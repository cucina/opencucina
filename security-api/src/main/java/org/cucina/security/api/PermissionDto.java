package org.cucina.security.api;

import java.io.Serializable;

import java.util.Collection;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class PermissionDto
    implements Serializable {
    private static final long serialVersionUID = 2532515604491906815L;
    private Collection<DimensionDto> dimensions;

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
}
