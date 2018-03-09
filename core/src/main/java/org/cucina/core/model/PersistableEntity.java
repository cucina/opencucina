package org.cucina.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.core.model.projection.ProjectionColumn;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
@MappedSuperclass
public abstract class PersistableEntity
		implements Persistable<Long> {
	/**
	 * cucina_sequence
	 */
	public static final String SEQUENCE_NAME = "cucina_sequence";
	/**
	 * id
	 */
	public static final String ID_PROPERTY = "id";
	/**
	 * This is a field JAVADOC
	 */
	public static final String APPLICATION_TYPE = "applicationType";
	private static final long serialVersionUID = 1L;
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
	 * @return JAVADOC.
	 */
	@Id
	//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdSeq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ProjectionColumn
	public Long getId() {
		return id;
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
	@Override
	@JsonIgnore
	@Transient
	public boolean isNew() {
		return id == null;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param obj JAVADOC.
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
