package org.cucina.security.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;


/**
 * Does not exist by itself, only in a user's context
 *
 * @author vlevine
 */
public class Preference {
	@NotNull
	private String name;
	private String value;

	/**
	 * Get name of search
	 *
	 * @return name String.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of search
	 *
	 * @param name String.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param value JAVADOC.
	 */
	public void setValue(String value) {
		this.value = value;
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

		if (!(obj instanceof Preference)) {
			return false;
		}

		Preference rhs = (Preference) obj;

		return new EqualsBuilder().append(this.getName(), rhs.getName()).isEquals();
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(15, 31).append(this.getName()).toHashCode();
	}
}
