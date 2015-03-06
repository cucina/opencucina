package org.cucina.security.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cucina.audit.Historised;
import org.cucina.core.model.PersistableEntity;
import org.cucina.core.model.Versioned;
import org.cucina.core.model.projection.PostProcessProjections;
import org.cucina.core.model.projection.ProjectionColumn;
import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.core.validation.Create;
import org.cucina.core.validation.Update;
import org.cucina.email.service.EmailUser;
import org.cucina.security.crypto.Encryptor;
import org.cucina.security.validation.ValidUsername;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.eclipse.persistence.annotations.Customizer;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.foundation.AbstractDirectMapping;
import org.eclipse.persistence.sessions.Session;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Out-of-the-box persistable User with named queries, validations and JPA
 * mappings. Just include it into the persistence.xml
 *
 * @author thornton
 * @author vlevine
 */

// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@PostProcessProjections
@Entity
@Cache(coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS)
@Customizer(Historised.class)
@Table(name = "UserTable")
public class User
    extends PersistableEntity
    implements UserDetails, Versioned, EmailUser {
    private static final long serialVersionUID = -9065653286223567115L;
    private static final Collection<GrantedAuthority> DEFAULT_AUTHORITIES = Collections.singleton((GrantedAuthority) new SimpleGrantedAuthority(
                "NONE"));
    private Collection<Permission> permissions = new HashSet<Permission>();
    private Locale locale;
    private String email;
    private String name;
    private String password;
    private String username;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean active;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private boolean system;
    private int version;

    /**
     * Using encryptor defined in SpringContext.
     *
     * @param password
     *
     * @return encrypted or plain if encryptor is not set.
     */
    public static String decrypt(String password) {
        Encryptor encryptor = findEncryptor();

        if (encryptor == null) {
            return password;
        }

        return encryptor.decrypt(password);
    }

    /**
     * Using encryptor defined in SpringContext.
     *
     * @param password
     *
     * @return encrypted or plain if encryptor is not set.
     */
    public static String encrypt(String password) {
        Encryptor encryptor = findEncryptor();

        if (encryptor == null) {
            return password;
        }

        return encryptor.encrypt(password);
    }

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
    @Column(nullable = false)
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
    @Column(nullable = false)
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Set if this User is active or not
     *
     * @param active
     *            boolean.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Is this User active
     *
     * @return active boolean.
     */
    @ProjectionColumn
    @Override
    @Column(nullable = false)
    public boolean isActive() {
        return active;
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
    @Column(nullable = false)
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
    @ProjectionColumn
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
    @Column(nullable = false)
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
    @Converter(converterClass = LocaleConverter.class, name = "localeConverter")
    @Convert("localeConverter")
    @Override
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
    @Converter(converterClass = PasswordConverter.class, name = "passwordConverter")
    @Convert("passwordConverter")
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
    @ManyToMany(fetch = FetchType.EAGER, cascade =  {
        CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinTable(name = "PERMISSION_USER", joinColumns = @JoinColumn(name = "USER_ID")
    , inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
    )
    public Collection<Permission> getPermissions() {
        return permissions;
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
    @ProjectionColumn
    @Column(nullable = false)
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
    @ProjectionColumn
    @NotNull(groups =  {
        Default.class, Create.class, Update.class}
    )
    @Basic(optional = false)
    @Column(unique = true, nullable = false)
    @Size(min = 1, max = 20, groups =  {
        Default.class, Create.class, Update.class}
    )
    @ValidUsername(groups =  {
        Default.class}
    )
    public String getUsername() {
        return username;
    }

    /**
     * Set version
     *
     * @param version
     *            int.
     */
    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Get version number
     *
     * @return version.
     */
    @Version
    @Override
    public int getVersion() {
        return version;
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

    private static Encryptor findEncryptor() {
        if (!SingletonBeanFactory.getInstance().containsBean("encryptor")) {
            return null;
        }

        return (Encryptor) SingletonBeanFactory.getInstance().getBean("encryptor");
    }

    public static class LocaleConverter
        implements org.eclipse.persistence.mappings.converters.Converter {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isMutable() {
            return true;
        }

        @Override
        public Object convertDataValueToObjectValue(Object arg0, Session arg1) {
            return LocaleUtils.toLocale((String) arg0);
        }

        @Override
        public Object convertObjectValueToDataValue(Object arg0, Session arg1) {
            return (arg0 == null) ? null : arg0.toString();
        }

        @Override
        public void initialize(DatabaseMapping mapping, Session session) {
            if (mapping.isAbstractDirectMapping()) {
                AbstractDirectMapping directMapping = (AbstractDirectMapping) mapping;

                directMapping.setFieldClassification(String.class);
            }
        }
    }

    public static class PasswordConverter
        implements org.eclipse.persistence.mappings.converters.Converter {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isMutable() {
            return true;
        }

        @Override
        public Object convertDataValueToObjectValue(Object arg0, Session arg1) {
            if (arg0 == null) {
                return null;
            }

            return User.decrypt((String) arg0);
        }

        @Override
        public Object convertObjectValueToDataValue(Object arg0, Session arg1) {
            if (arg0 == null) {
                return null;
            }

            return User.encrypt((String) arg0);
        }

        @Override
        public void initialize(DatabaseMapping mapping, Session session) {
            if (mapping.isAbstractDirectMapping()) {
                AbstractDirectMapping directMapping = (AbstractDirectMapping) mapping;

                directMapping.setFieldClassification(String.class);
            }
        }
    }
}
