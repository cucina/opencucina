package org.cucina.i18n.model;

import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import javax.validation.constraints.NotNull;

import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.core.model.projection.PostProcessProjections;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.core.model.support.BooleanConverter;
import org.cucina.core.model.support.JsonMapConverter;
import org.cucina.core.validation.NotBlank;

import org.cucina.i18n.validation.UniqueListNode;


/**
 * A representation of a category node.
 */
@Entity
@Cacheable
@UniqueListNode
@PostProcessProjections
public class ListNode
    extends PersistableEntity
    implements Versioned {
    private static final long serialVersionUID = 1L;

    // Mark this ListNode as the default
    @NotNull
    private Boolean defaultValue = false;

    // Mark this ListNode as retired
    @NotNull
    private Boolean retired = false;
    private Map<String, Object> attributes;

    // The name of the object
    @NotNull
    private Message label;

    // The type of ListNode
    @NotNull
    @NotBlank
    private String type;
    private int version;

    /**
     * Creates a new instance of the {@link ListNode} class.
     */
    public ListNode() {
    }

    /**
     * Creates a new instance of the {@link ListNode} class specifying
     * the underlying application type of this node instance.
     *
     * @param type the application type of this node instance.
     */
    public ListNode(String type) {
        setType(type);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param attributes JAVADOC.
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */

    //    @Lob
    @Convert(converter = JsonMapConverter.class)
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Sets the defaultValue flag on the object
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the defaultValue flag on the object
     * @return Returns the defaultValue.
     */
    @Convert(converter = BooleanConverter.class)
    @Column(columnDefinition = "CHAR(1) not null")
    @ProjectionColumn
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets name of the object.
     *
     * @param label the name of the object
     */
    public void setLabel(Message label) {
        this.label = label;
    }

    /**
     * Gets the label of the object.
     *
     * @return the label of the object
     */
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @ProjectionColumn
    public Message getLabel() {
        return label;
    }

    /**
     * @param retired The retired to set.
     */
    public void setRetired(Boolean retired) {
        this.retired = retired;
    }

    /**
     * @return Returns the retired.
     */
    @ProjectionColumn
    @Convert(converter = BooleanConverter.class)
    @Column(columnDefinition = "CHAR(1) not null")
    public Boolean getRetired() {
        return retired;
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
    @ProjectionColumn
    public String getType() {
        return type;
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
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public String toString() {
        return "[" + getApplicationType() + ":" +
        ((getLabel() == null) ? "'null label'" : getLabel().getMessageCd()) + " id=" + getId() +
        "]";
    }
}
