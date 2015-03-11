package org.cucina.core.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@Entity
@Cacheable
public class Attachment
    extends PersistableEntity {
    private static final long serialVersionUID = 2114088180516791908L;
    private String filename;
    private String type;
    private byte[] data;

    /**
     * JAVADOC Method Level Comments
     *
     * @param data JAVADOC.
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Lob
    @Column(length = 1024 * 1024, columnDefinition = "blob")
    @Basic(fetch = FetchType.LAZY)
    public byte[] getData() {
        return data;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param filename JAVADOC.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param type JAVADOC.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getType() {
        return type;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                                        .append("filename", getFilename()).toString();
    }
}
