package org.cucina.security;

import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.collections.CollectionUtils;
import org.cucina.core.InstanceFactory;
import org.cucina.security.crypto.PasswordDecryptor;
import org.cucina.security.model.Permission;
import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.repository.PermissionRepository;
import org.cucina.security.repository.RoleRepository;
import org.cucina.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class UserPasswordSetterImpl
    implements UserPasswordSetter {
    private static final Logger LOG = LoggerFactory.getLogger(UserPasswordSetterImpl.class);
    private InstanceFactory instanceFactory;
    private PasswordDecryptor passwordDecryptor;
    private PermissionRepository permissionRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    /**
     * Creates a new UserPasswordSetterImpl object.
     *
     * @param persistenceService
     *            JAVADOC.
     * @param userDao
     *            JAVADOC.
     */
    public UserPasswordSetterImpl(UserRepository userRepository, InstanceFactory instanceFactory,
        PasswordDecryptor passwordDecryptor, RoleRepository roleRepository,
        PermissionRepository permissionRepository) {
        Assert.notNull(userRepository, "userRepository is null");
        this.userRepository = userRepository;
        Assert.notNull(instanceFactory, "instanceFactory is null");
        this.instanceFactory = instanceFactory;
        Assert.notNull(passwordDecryptor, "passwordDecryptor is null");
        this.passwordDecryptor = passwordDecryptor;
        Assert.notNull(roleRepository, "roleRepository is null");
        this.roleRepository = roleRepository;
        Assert.notNull(permissionRepository, "permissionRepository is null");
        this.permissionRepository = permissionRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username
     *            JAVADOC.
     * @param password
     *            JAVADOC.
     */
    @Transactional
    @Override
    public void setUserPassword(String username, String password) {
        User user = (User) userRepository.loadUserByUsername(username);

        user.setPassword(password);

        userRepository.save(user);
    }

    /**
     * Either updates an existing admin user with given password or creates new
     * one. Disables other administrators, makes certain that ADMINISTRATOR
     * permissions are assigned only to this user.
     *
     * @param username
     *            JAVADOC.
     * @param password
     *            JAVADOC.
     */
    @Override
    @Transactional
    public void createAdminUser(String username, String password) {
        Collection<User> users = userRepository.findBySystem(true);
        User system = null;

        for (User user : users) {
            if (username.equals(user.getUsername())) {
                system = user;
            } else {
                user.setActive(false);
                user.setEnabled(false);
                userRepository.save(user);
            }
        }

        if (system == null) {
            system = instanceFactory.getBean(User.class.getSimpleName());
        }

        system.setSystem(true);
        system.setAccountNonExpired(true);
        system.setAccountNonLocked(true);
        system.setActive(true);
        system.setEnabled(true);
        system.setCredentialsNonExpired(true);
        system.setName(username);
        system.setUsername(username);
        system.setPassword(passwordDecryptor.decrypt(password));

        Collection<Permission> permissions = system.getPermissions();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Existing permissions:" + permissions);
        }

        boolean admin = false;
        Role adminRole = roleRepository.findByName(Role.ADMINISTRATOR);

        Assert.notNull(adminRole, "adminRole is null");

        for (Permission permission : permissions) {
            if (permission.getRole().equals(adminRole)) {
                admin = true;
            }
        }

        if (!admin) {
            LOG.debug("Creating new permission");

            Collection<Permission> perms = permissionRepository.findByRole(adminRole);
            Permission perm = null;

            for (Permission permission : perms) {
                //Find a permission with no dimensions as they're not relevant for the admin user.
                if (CollectionUtils.isEmpty(permission.getDimensions())) {
                    perm = permission;

                    break;
                }
            }

            if (perm == null) {
                perm = instanceFactory.getBean(Permission.TYPE);
                perm.setName(Role.ADMINISTRATOR);
                perm.setRole(adminRole);
            }

            perm.getUsers().add(system);

            Collection<Permission> adPerms = new HashSet<Permission>();

            adPerms.add(perm);
            system.setPermissions(adPerms);
        }

        userRepository.save(system);

        // TODO update savedsearches with admin user as owner. only for
        // specified savedsearches.
    }
}
