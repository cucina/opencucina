package org.cucina.security.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.cucina.security.validation.ValidUsername;


/**
 *
 * @author vlevine
 */
@Document
public class User
    extends Entity
    implements UserDetails {
    private static final long serialVersionUID = -9065653286223567115L;
    private static final Collection<GrantedAuthority> DEFAULT_AUTHORITIES = Collections.singleton((GrantedAuthority) new SimpleGrantedAuthority(
                "NONE"));
    private Collection<Permission> permissions = new HashSet<Permission>();
    private Collection<Preference> preferences = new HashSet<Preference>();
    private Locale locale;
    private String email;
    private String name;
    private String password;
    private String username;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private boolean system;

    /**
     * JAVADOC Method Level Comments
     *
     * @param accountNonExpired
     *            JAVADOC.
     */
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param accountNonLocked
     *            JAVADOC.
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Get granted authorities as collection of
     * SimpleGrantedAuthorities(roleName) for each role:
     *
     * TODO should be used by permission manager
     *
     * @return JAVADOC.
     */
    @SuppressWarnings("unchecked")
    @Override
    @Transient
    public Collection<?extends GrantedAuthority> getAuthorities() {
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
     * @param credentialsNonExpired
     *            JAVADOC.
     */
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
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
     * Get email
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param enabled
     *            JAVADOC.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param locale
     *            JAVADOC.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
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
     * @param name
     *            JAVADOC.
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

    /**
     * JAVADOC Method Level Comments
     *
     * @param password
     *            JAVADOC.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get password from schema User
     *
     * @return password String.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param permissions
     *            JAVADOC.
     */
    public void setPermissions(Collection<Permission> permissions) {
        this.permissions = permissions;
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
     *
     *
     * @param preferences .
     */
    public void setPreferences(Collection<Preference> preferences) {
        this.preferences = preferences;
    }

    /**
     *
     *
     * @return .
     */
    public Collection<Preference> getPreferences() {
        return preferences;
    }

    /**
     * If this is an admin User
     *
     * @param admin
     *            boolean.
     */
    public void setSystem(boolean system) {
        this.system = system;
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
     * Set username
     *
     * @param username
     *            String.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get username
     *
     * @return username.
     */
    @NotNull
    @ValidUsername(groups =  {
        Default.class}
    )
    public String getUsername() {
        return username;
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
