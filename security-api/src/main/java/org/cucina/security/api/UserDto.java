package org.cucina.security.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Collection;


/**
 * @author vlevine
 */
public class UserDto
		implements Serializable {
	private static final long serialVersionUID = 4803992322689471938L;
	private Collection<PermissionDto> permissions;
	private Collection<PreferenceDto> preferences;
	private Long id;
	private String email;
	private String password;
	private String username;

	/**
	 * @return .
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email .
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return .
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id .
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return .
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password .
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return .
	 */
	public Collection<PermissionDto> getPermissions() {
		return permissions;
	}

	/**
	 * @param permissions .
	 */
	public void setPermissions(Collection<PermissionDto> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return .
	 */
	public Collection<PreferenceDto> getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences .
	 */
	public void setPreferences(Collection<PreferenceDto> preferences) {
		this.preferences = preferences;
	}

	/**
	 * @return .
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username .
	 */
	public void setUsername(String username) {
		this.username = username;
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
