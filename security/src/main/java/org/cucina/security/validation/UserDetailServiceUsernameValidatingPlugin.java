package org.cucina.security.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UserDetailServiceUsernameValidatingPlugin
    implements UsernameValidatingPlugin {
    private static final Logger LOG = LoggerFactory.getLogger(UserDetailServiceUsernameValidatingPlugin.class);
    private String MESSAGE = "{org.cucina.meringue.security.validation.ValidUsername.unique}";
    private UserDetailsService userDetailsService;

    /**
     * Creates a new UserDaoUsernameValidatingPlugin object.
     *
     * @param userDao JAVADOC.
     */
    public UserDetailServiceUsernameValidatingPlugin(UserDetailsService userRepository) {
        Assert.notNull(userRepository, "userRepository is null");
        this.userDetailsService = userRepository;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isValid(String username) {
        try {
            return userDetailsService.loadUserByUsername(username) == null;
        } catch (UsernameNotFoundException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No user with id '" + username + "' found");
            }

            return true;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public String message() {
        return MESSAGE;
    }
}
