
package org.cucina.security.authentication;

import org.cucina.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AuthenticationSuccessEventHandler
    implements ApplicationListener<AuthenticationSuccessEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationSuccessEventHandler.class);
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
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("received successful login event for pricipal: " +
                event.getAuthentication().getPrincipal());
        }

        Assert.isInstanceOf(User.class, event.getAuthentication().getPrincipal(), "Should be a user");

        String username = ((User) event.getAuthentication().getPrincipal()).getUsername();

        loginAttemptsHandler.loginSuccess(username);
    }
}
