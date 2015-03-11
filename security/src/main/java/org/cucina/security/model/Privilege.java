package org.cucina.security.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.projection.PostProcessProjections;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.core.validation.NotBlank;

import org.cucina.security.validation.UniquePrivilege;


/**
 * Permission object belonging to a {@link Role}.
 *
 * @author $Author: thornton $
 * @version $Revision: 1.4 $
  */
@Entity
@Cacheable
@PostProcessProjections
public class Privilege
    extends PersistableEntity {
    private static final long serialVersionUID = -2662069139645257874L;

    /** Privilege */
    public static final String TYPE = "Privilege";
    private Collection<Role> roles = new HashSet<Role>();
    @UniquePrivilege
    @NotNull
    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9_\\-]+", message = "error.alphanumeric")
    private String name;

    /**
    * Get applicationType
    *
    * @return applicationType String.
    */
    @Override
    @Transient
    public String getApplicationType() {
        return TYPE;
    }

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
     * @return name.
     */
    @Basic(optional = false)
    @Column(unique = true, nullable = false)
    @ProjectionColumn
    public String getName() {
        return name;
    }

    /**
     * Set roles
     *
     * @param roles Collection<Role>.
     */
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    /**
     * Get roles
     *
     * @return roles Collection<Role>.
     */
    @ManyToMany(cascade =  {
        CascadeType.PERSIST, CascadeType.MERGE}
    , mappedBy = "privileges")
    public Collection<Role> getRoles() {
        return roles;
    }
}
