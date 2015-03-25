package org.cucina.security.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.core.model.projection.PostProcessProjections;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.security.validation.UniquePermission;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Entity
@Cacheable
@UniquePermission
@PostProcessProjections
public class Permission
    extends PersistableEntity
    implements Versioned {
    private static final long serialVersionUID = -5353620819000911436L;

    /** This is a field JAVADOC */
    public static final String TYPE = "Permission";

    /** PERMISSION_DIMENSION. */
    public static final String JOIN_TABLE_DIMENSION = "PERMISSION_DIMENSION";

    /** PERMISSION_ID. */
    public static final String JOIN_PERMISSION = "PERMISSION_ID";

    /** DIMENSION_ID. */
    public static final String JOIN_DIMENSION = "DIMENSION_ID";

    /** User.withPermission */
    private Collection<Dimension> dimensions = new HashSet<Dimension>();
    private Collection<User> users = new HashSet<User>();
    private Role role;
    private String name;
    private int version;

    /**
     * JAVADOC Method Level Comments
     *
     * @param dimensions JAVADOC.
     */
    public void setDimensions(Collection<Dimension> dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @OneToMany(cascade =  {
        CascadeType.ALL}
    , mappedBy = "permission")
    @ProjectionColumn
    public Collection<Dimension> getDimensions() {
        return dimensions;
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @param name JAVADOC.
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ProjectionColumn
    public String getName() {
        return name;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param role JAVADOC.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "role")
    @ProjectionColumn
    public Role getRole() {
        return role;
    }

    /**
     * Set permissions
     *
     * @param permissions Collection<Permission>
     */
    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    /**
     * Get permissions
     *
     * @return permissions Collection<Permission>
     */
    @ManyToMany(mappedBy = "permissions", cascade =  {
        CascadeType.PERSIST}
    )
    @ProjectionColumn
    public Collection<User> getUsers() {
        return users;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param version JAVADOC.
     */
    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    @Version
    public int getVersion() {
        return version;
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
