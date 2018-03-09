package org.cucina.security.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.security.validation.UniqueRole;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;

/**
 * Role contains {@link Privilege}s. Can exist by itself (as a template)
 * accessible via RoleRepository or in a Permission, as part of user's access
 * rights.
 *
 * @author vlevine
 */
public class Role extends Entity {
	/**
	 * This is a field JAVADOC
	 */
	public static final String ADMINISTRATOR = "ADMINISTRATOR";
	private Collection<Privilege> privileges = new HashSet<Privilege>();
	@UniqueRole
	@Size(min = 1, max = 32)
	@NotNull
	@Pattern(regexp = "[a-zA-Z0-9_\\-]+", message = "error.alphanumeric")
	private String name;

	/**
	 * Get name
	 *
	 * @return name String.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name
	 *
	 * @param name .
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get privileges
	 *
	 * @return privileges Collection<Privilege>.
	 */
	public Collection<Privilege> getPrivileges() {
		return privileges;
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
	 * Default toString implementation
	 *
	 * @return This object as String.
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
