package org.cucina.security.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.cucina.core.model.PersistableEntity;


/**
 * Out-of-the-box persistable Preference with JPA mappings. Just include it into
 * the persistence.xml. Depends on User from the same package.
 *
 * @author $Author: vlevine $
 * @version $Revision: 1.1 $
 */
@Entity
@Cacheable
public class Preference
    extends PersistableEntity {
    private static final long serialVersionUID = -2407283406513275282L;
    private String name;
    private String value;
    private User owner;

    /**
     * Set name of search
     *
     * @param name
     *            String.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get name of search
     *
     * @return name String.
     */
    @Basic(optional = false)
    public String getName() {
        return name;
    }

    /**
     * Set owner
     *
     * @param owner
     *            User.
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Get owner
     *
     * @return owner User.
     */
    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "owner")
    public User getOwner() {
        return owner;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param value
     *            JAVADOC.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Column(length = 4000)
    public String getValue() {
        return value;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param obj
     *            JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Preference)) {
            return false;
        }

        Preference rhs = (Preference) obj;

        return new EqualsBuilder().append(this.getOwner(), rhs.getOwner())
                                  .append(this.getName(), rhs.getName()).isEquals();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(15, 31).append(this.getOwner()).append(this.getName())
                                          .toHashCode();
    }
}
