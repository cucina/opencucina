package org.cucina.security.model;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.security.validation.ValidUsername;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;


/**
 * @author vlevine
 */
@Document
public class User
		extends Entity {
	private static final Collection<GrantedAuthority> DEFAULT_AUTHORITIES = Collections.singleton((GrantedAuthority) new SimpleGrantedAuthority(
			"NONE"));
	private Collection<Permission> permissions = new HashSet<Permission>();
	private Collection<Preference> preferences = new HashSet<Preference>();
	private Locale locale;
	private Password password;
	private String email;
	private String name;
	private String username;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private boolean system;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param accountNonExpired JAVADOC.
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param accountNonLocked JAVADOC.
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * Get granted authorities as collection of
	 * SimpleGrantedAuthorities(roleName) for each role:
	 * <p>
	 * TODO should be used by permission manager
	 *
	 * @return JAVADOC.
	 */
	@SuppressWarnings("unchecked")
	@Transient
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<Permission> permissions = getPermissions();

		if (permissions == null) {
			return DEFAULT_AUTHORITIES;
		}

		return CollectionUtils.collect(permissions,
				new Transformer() {
					@Override
					public Object transform(Object arg0) {
						Permission permission = (Permission) arg0;
						BeanWrapper beanWrapper = new BeanWrapperImpl(permission.getRole());
						String name = (String) beanWrapper.getPropertyValue("name");

						return new SimpleGrantedAuthority(name);
					}
				});
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param credentialsNonExpired JAVADOC.
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * Get email
	 *
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set email
	 *
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param enabled JAVADOC.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param locale JAVADOC.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
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
	 * @param password JAVADOC.
	 */
	public void setPassword(Password password) {
		this.password = password;
	}

	/**
	 * Get password from schema User
	 *
	 * @return password String.
	 */
	public Password getPassword() {
		return password;
	}

	/**
	 * @param pass .
	 */
	public void setPassword(String pass) {
		this.password = new Password(pass);
	}

	/**
	 * Get permissions
	 *
	 * @return permissions Collection<Role>.
	 */
	public Collection<Permission> getPermissions() {
		return permissions;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param permissions JAVADOC.
	 */
	public void setPermissions(Collection<Permission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return .
	 */
	public Collection<Preference> getPreferences() {
		return preferences;
	}

	/**
	 * @param preferences .
	 */
	public void setPreferences(Collection<Preference> preferences) {
		this.preferences = preferences;
	}

	/**
	 * If this is an admin User
	 *
	 * @return admin boolean.
	 */
	public boolean isSystem() {
		return system;
	}

	/**
	 * If this is an admin User
	 *
	 * @param admin boolean.
	 */
	public void setSystem(boolean system) {
		this.system = system;
	}

	/**
	 * Get username
	 *
	 * @return username.
	 */
	@NotNull
	@ValidUsername(groups = {
			Default.class}
	)
	public String getUsername() {
		return username;
	}

	/**
	 * Set username
	 *
	 * @param username String.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("username", username)
				.toString();
	}
}
