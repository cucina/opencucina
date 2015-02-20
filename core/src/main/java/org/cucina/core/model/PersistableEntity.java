package org.cucina.core.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.cucina.core.model.projection.ProjectionColumn;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.ObjectTypeConverter;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
@MappedSuperclass
@ObjectTypeConverter(name = "booleanConverter", dataType = java.lang.String.class, objectType = java.lang.Boolean.class, conversionValues =  {
    @ConversionValue(dataValue = "1", objectValue = "true")
    , @ConversionValue(dataValue = "0", objectValue = "false")
}
)
@SequenceGenerator(name = "IdSeq", sequenceName = PersistableEntity.SEQUENCE_NAME, allocationSize = 10)
public abstract class PersistableEntity
    implements Persistable<Long> {
    private static final long serialVersionUID = 1L;

    /** cucina_sequence */
    public static final String SEQUENCE_NAME = "cucina_sequence";

    /** id */
    public static final String ID_PROPERTY = "id";
    private Long id;

    /**
     * Application type of this object
     *
     * @return short name of the class.
     */
    @Transient
    @JsonIgnore
    public String getApplicationType() {
        return this.getClass().getSimpleName();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param id JAVADOC.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdSeq")
    @ProjectionColumn
    public Long getId() {
        return id;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param obj JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PersistableEntity)) {
            return false;
        }

        PersistableEntity rhs = (PersistableEntity) obj;

        if ((getId() != null) || (rhs.getId() != null)) {
            return new EqualsBuilder().append(getId(), rhs.getId()).isEquals();
        }

        return this.hashCode() == rhs.hashCode();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public int hashCode() {
        if (getId() != null) {
            return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
        }

        return super.hashCode();
    }

    /**
    * JAVADOC Method Level Comments
    *
    * @return JAVADOC.
    */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append(PersistableEntity.ID_PROPERTY, getId()).toString();
    }
}
