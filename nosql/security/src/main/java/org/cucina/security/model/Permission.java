package org.cucina.security.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.HashSet;


/**
 * User access control. Can only exist as a part of a user.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class Permission {
	/**
	 * User.withPermission
	 */
	private Collection<Dimension> dimensions = new HashSet<Dimension>();
	private Role role;
	private String name;

	/**
	 * @return .
	 */
	public Collection<Dimension> getDimensions() {
		return dimensions;
	}

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
	public String getName() {
		return name;
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
	public Role getRole() {
		return role;
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
	 * Default toString implementation
	 *
	 * @return This object as String.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
