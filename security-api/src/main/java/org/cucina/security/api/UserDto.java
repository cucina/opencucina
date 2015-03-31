package org.cucina.security.api;

import java.io.Serializable;

import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 *
 *
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
     *
     *
     * @param email .
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     *
     * @return .
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     *
     * @param id .
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     *
     * @return .
     */
    public Long getId() {
        return id;
    }

    /**
     *
     *
     * @param password .
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     *
     * @return .
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     *
     * @param permissions .
     */
    public void setPermissions(Collection<PermissionDto> permissions) {
        this.permissions = permissions;
    }

    /**
     *
     *
     * @return .
     */
    public Collection<PermissionDto> getPermissions() {
        return permissions;
    }

    /**
     *
     *
     * @param preferences .
     */
    public void setPreferences(Collection<PreferenceDto> preferences) {
        this.preferences = preferences;
    }

    /**
     *
     *
     * @return .
     */
    public Collection<PreferenceDto> getPreferences() {
        return preferences;
    }

    /**
     *
     *
     * @param username .
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     *
     * @return .
     */
    public String getUsername() {
        return username;
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
