
package org.cucina.audit;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.util.Assert;


/**
 * Holds new and old property values where the property is deemed to
 * have changed.

 *
 */
public class AuditDifference {
    private Object newValue;
    private Object oldValue;
    private String property;

    /**
     * Creates a new AuditDifference object.
     *
     * @param property JAVADOC.
     * @param newValue JAVADOC.
     * @param oldValue JAVADOC.
     */
    public AuditDifference(String property, Object newValue, Object oldValue) {
        Assert.hasText(property, "property is required!");
        this.property = property;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Object getNewValue() {
        return newValue;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Check the property name.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof AuditDifference)) {
            return false;
        }

        AuditDifference other = (AuditDifference) obj;

        return new EqualsBuilder().append(this.property, other.property).isEquals();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.property).toHashCode();
    }
}
