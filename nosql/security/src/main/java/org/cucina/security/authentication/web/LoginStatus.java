
package org.cucina.security.authentication.web;


/**
 * Holds the user's login status.

 *
 */
public class LoginStatus {
    private final String errorMessage;
    private final String username;
    private final boolean loggedIn;
    private final boolean success;

    /**
     * Creates a new LoginStatus object.
     *
     * @param success JAVADOC.
     * @param loggedIn JAVADOC.
     * @param username JAVADOC.
     * @param errorMessage JAVADOC.
     */
    public LoginStatus(boolean success, boolean loggedIn, String username, String errorMessage) {
        this.success = success;
        this.loggedIn = loggedIn;
        this.username = username;
        this.errorMessage = errorMessage;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public String getUsername() {
        return username;
    }
}
