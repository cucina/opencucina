package org.cucina.security;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class SystemUserServiceImpl
    implements SystemUserService, InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(SystemUserServiceImpl.class);
    private String password;
    private String username;
    private UserPasswordSetter userPasswordSetter;
    private UserRepository userRepository;

    /**
     * Creates a new SystemUserServiceImpl object.
     *
     * @param instanceFactory
     *            JAVADOC.
     * @param persistenceService
     *            JAVADOC.
     */
    public SystemUserServiceImpl(UserRepository userRepository) {
        Assert.notNull(userRepository, "userRepository is null");
        this.userRepository = userRepository;
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
     * JAVADOC Method Level Comments
     *
     * @param userPasswordSetter JAVADOC.
     */
    public void setUserPasswordSetter(UserPasswordSetter userPasswordSetter) {
        this.userPasswordSetter = userPasswordSetter;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username
     *            JAVADOC.
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
    public String getUsername() {
        if (StringUtils.isNotEmpty(username)) {
            createIfNeeded();

            return username;
        }

        Collection<User> users = userRepository.findBySystem(true);

        if (CollectionUtils.isEmpty(users)) {
            LOG.error("CONFIGURATION ERROR - No system user available!");
            throw new IllegalArgumentException("Failed to find any system users");
        }

        return users.iterator().next().getUsername();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Override
    public void afterPropertiesSet()
        throws Exception {
        if (StringUtils.isNotEmpty(username)) {
            Assert.notNull(userPasswordSetter,
                "userPasswordSetter required if username is not empty");
        }
    }

    /**
     * Creates a system user if one is specified in properties.
     *
     * @throws Exception
     *             JAVADOC.
     */
    private synchronized void createIfNeeded() {
        try {
            userPasswordSetter.createAdminUser(username, password);
        } catch (Exception e) {
            // TODO an exception here could happen in cluster env if another
            // node had inserted user just before this one. how to handle?
            if (LOG.isDebugEnabled()) {
                LOG.warn("Assume that user '" + username + "' exists", e);
            } else {
                LOG.warn("Assume that user '" + username + "' exists");
            }
        }
    }
}
