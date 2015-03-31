package org.cucina.security.authentication;


/**
 * Locks the user account once they've exceeded the acceptable number of
 * consecutive attempts.
 *
 */
public interface LoginAttemptsHandler {
    /**
     * Clears the current consecutive failed logins for the username.
     *
     * @param username
     */
    void loginSuccess(String username);

    /**
     * Register a failed attempt by the user.
     *
     * @param username
     */
    void registerFailedAttempt(String username);
}
