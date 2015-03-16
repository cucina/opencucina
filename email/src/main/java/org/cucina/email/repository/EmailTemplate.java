package org.cucina.email.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.cucina.core.model.PersistableEntity;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
  */
@Entity
public class EmailTemplate
    extends PersistableEntity {
    private static final long serialVersionUID = 293452782053205046L;
    private Date lastModified;
    private String body;
    private String name;

    /**
     * JAVADOC Method Level Comments
     *
     * @param body JAVADOC.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getBody() {
        return body;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param lastModified JAVADOC.
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return lastModified;
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
    public String getName() {
        return name;
    }

    @PrePersist
    void createdAt() {
        lastModified = new Date();
    }

    @PreUpdate
    void updatedAt() {
        lastModified = new Date();
    }
}
