package org.cucina.security.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.core.model.projection.PostProcessProjections;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.core.validation.NotBlank;

import org.cucina.loader.LoaderColumnLookup;

import org.cucina.security.validation.UniqueRole;


/**
 * Permission object which contains {@link Privilege}s.
 *
 * @author thornton
 * @author vlevine
 * @version $Revision: 1.4 $
  */
@Entity
@Cacheable
@PostProcessProjections
public class Role
    extends PersistableEntity
    implements Versioned {
    /** This is a field JAVADOC */
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    private static final long serialVersionUID = -7667946490176006761L;

    /** Role */
    public static final String TYPE = "Role";

    /** ROLE_PRIVILEGE. */
    public static final String JOIN_TABLE = "ROLE_PRIVILEGE";

    /** ROLE_ID. */
    public static final String JOIN_ROLE = "ROLE_ID";

    /** PRIVILEGE_ID. */
    public static final String JOIN_PRIVILEGE = "PRIVILEGE_ID";
    private Collection<Privilege> privileges = new HashSet<Privilege>();
    @UniqueRole
    @Size(min = 1, max = 32)
    @NotBlank
    @NotNull
    @LoaderColumnLookup(propertyAlias =  {
        "Role Name", "roleName"}
    )
    @Pattern(regexp = "[a-zA-Z0-9_\\-]+", message = "error.alphanumeric")
    private String name;
    private int version;

    /**
    * Set name
    *
    * @param name.
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get name
     *
     * @return name String.
     */
    @Basic(optional = false)
    @Column(unique = true, nullable = false)
    @ProjectionColumn
    public String getName() {
        return name;
    }

    /**
     * Set privileges
     *
     * @param privileges Collection<Privilege>.
     */
    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

    /**
     * Get privileges
     *
     * @return privileges Collection<Privilege>.
     */
    @ManyToMany(cascade =  {
        CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(name = JOIN_TABLE, joinColumns = @JoinColumn(name = JOIN_ROLE)
    , inverseJoinColumns = @JoinColumn(name = JOIN_PRIVILEGE)
    )
    @ProjectionColumn
    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param version JAVADOC.
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
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
