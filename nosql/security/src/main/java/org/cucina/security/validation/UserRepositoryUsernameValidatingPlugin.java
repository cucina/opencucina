package org.cucina.security.validation;

import org.springframework.util.Assert;

import org.cucina.security.repository.UserRepository;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UserRepositoryUsernameValidatingPlugin
    implements UsernameValidatingPlugin {
    private String MESSAGE = "{org.cucina.security.validation.ValidUsername.unique}";
    private UserRepository userRepository;

    /**
     * Creates a new UserDaoUsernameValidatingPlugin object.
     *
     * @param userDao JAVADOC.
     */
    public UserRepositoryUsernameValidatingPlugin(UserRepository userRepository) {
        Assert.notNull(userRepository, "userRepository is null");
        this.userRepository = userRepository;
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
        return userRepository.findByUsername(username) == null;
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
