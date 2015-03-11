package org.cucina.security.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.core.validation.NotBlank;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Entity
@Cacheable
@Table(name = "Dimension")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dimension
    extends PersistableEntity
    implements Versioned {
    private static final long serialVersionUID = -987823374469744356L;

    /** This is a field JAVADOC */
    public static final String TYPE = "Dimension";
    private Long domainObjectId;
    private Permission permission;
    private PersistableEntity domainObject;
    private String domainObjectType;
    private String propertyName;
    private int version;

    /**
    * JAVADOC Method Level Comments
    *
    * @param domainObject JAVADOC.
    */
    public void setDomainObject(PersistableEntity domainObject) {
        this.domainObject = domainObject;
    }

    /**
     * The setting of domainObject is for convenience only, and should be ignored by underlying
     * ORM.
     *
     * @return domainObject PersistableObject.
     */
    @Transient
    public PersistableEntity getDomainObject() {
        return domainObject;
    }

    /**
    * JAVADOC.
    *
    * @param expiryDate
    *            JAVADOC.
    */
    public void setDomainObjectId(Long domainObjectId) {
        this.domainObjectId = domainObjectId;
    }

    /**
     * JAVADOC.
     *
     * @param expiryDate
     *            JAVADOC.
     */
    @Basic
    @Column(name = "domain_object_id", nullable = false)
    public Long getDomainObjectId() {
        return this.domainObjectId;
    }

    /**
     * JAVADOC.
     *
     * @param domainObjectType
     *            JAVADOC.
     */
    public void setDomainObjectType(String domainObjectType) {
        this.domainObjectType = domainObjectType;
    }

    /**
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    @Basic
    @Column(name = "domain_object_type", nullable = false)
    public String getDomainObjectType() {
        return domainObjectType;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param permission JAVADOC.
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @ManyToOne(optional = false)
    public Permission getPermission() {
        return this.permission;
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
    @NotBlank
    @Basic
    public String getPropertyName() {
        return propertyName;
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
}
