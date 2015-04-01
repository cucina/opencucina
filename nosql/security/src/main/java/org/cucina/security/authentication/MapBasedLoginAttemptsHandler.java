package org.cucina.security.authentication;

import java.util.HashMap;

import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


/**
 * Simple <code>FailedLoginAttemptsHandler</code> implementation which stores
 * the consecutive failed attempts for the user in a <code>Map</code>
 *
 */
public class MapBasedLoginAttemptsHandler
    implements LoginAttemptsHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MapBasedLoginAttemptsHandler.class);
    private HashMap<String, Integer> failedLoginAttempts = new HashMap<String, Integer>();
    private Integer maxAllowedAttempts;
    private UserRepository userRepository;

    /**
     * Creates a new MapBasedLoginAttemptsHandler object.
     *
     * @param userRepository
     *            JAVADOC.
     * @param maxAllowedAttempts
     *            JAVADOC.
     */
    public MapBasedLoginAttemptsHandler(UserRepository userRepository, Integer maxAllowedAttempts) {
        Assert.notNull(userRepository, "userRepository is null");
        this.userRepository = userRepository;
        Assert.notNull(maxAllowedAttempts, "maxAllowedAttempts is null");
        Assert.isTrue(maxAllowedAttempts > 0, "maxAllowedAttempts must be greater than zero!");
        this.maxAllowedAttempts = maxAllowedAttempts;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username
     *            JAVADOC.
     * @see org.cucina.meringue.security.ConsecutiveBadCredentialsHandler#loginSuccess
     *      (java.lang.String)
     */
    @Override
    public void loginSuccess(String username) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("reseting the failed login count for " + username);
        }

        failedLoginAttempts.remove(username);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username
     *            JAVADOC.
     * @see org.cucina.meringue.security.ConsecutiveBadCredentialsHandler#
     *      registerFailedAttempt(java.lang.String)
     */
    @Override
    @Transactional
    public void registerFailedAttempt(String username) {
        int attemptCnt = (failedLoginAttempts.get(username) == null) ? 0
                                                                     : failedLoginAttempts.get(username);

        attemptCnt += 1;

        if (LOG.isDebugEnabled()) {
            LOG.debug(username + " has " + attemptCnt + " failed attempts");
        }

        if (attemptCnt > maxAllowedAttempts) {
            User user = userRepository.findByUsername(username);

            if (user != null) {
                LOG.debug("locking account for " + username +
                    " as they've exceeded the number of failed login attempts");
                user.setAccountNonLocked(false);
                userRepository.save(user);
            }

            failedLoginAttempts.remove(username);
        } else {
            failedLoginAttempts.put(username, attemptCnt);
        }
    }
}
