package org.cucina.security.api;

import java.io.Serializable;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
public class DimensionDto
    implements Serializable {
	private static final long serialVersionUID = 5884988987152397986L;
	private Long domainObjectId;
    private String propertyName;

    /**
     * JAVADOC Method Level Comments
     *
     * @param domainObjectId JAVADOC.
     */
    public void setDomainObjectId(Long domainObjectId) {
        this.domainObjectId = domainObjectId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Long getDomainObjectId() {
        return domainObjectId;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param propertyName JAVADOC.
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getPropertyName() {
        return propertyName;
    }
}
