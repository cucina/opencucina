
package org.cucina.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AuthenticationBadCredentialsEventHandler
    implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFailureBadCredentialsEvent.class);
    private LoginAttemptsHandler loginAttemptsHandler;

    /**
     * JAVADOC Method Level Comments
     *
     * @param loginAttemptsHandler JAVADOC.
     */
    @Required
    public void setLoginAttemptsHandler(LoginAttemptsHandler loginAttemptsHandler) {
        this.loginAttemptsHandler = loginAttemptsHandler;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param event JAVADOC.
     */
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("received failed login event for pricipal: " +
                event.getAuthentication().getPrincipal());
        }

        Assert.isInstanceOf(String.class, event.getAuthentication().getPrincipal(),
            "Should be a username");

        String username = (String) event.getAuthentication().getPrincipal();

        loginAttemptsHandler.registerFailedAttempt(username);
    }
}
