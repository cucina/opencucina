
package org.cucina.security;

import org.cucina.security.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CurrentUserChannelInterceptor
    extends ChannelInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(CurrentUserChannelInterceptor.class);
    private AuthenticationService authenticationService;
    private SystemUserService systemUserService;

    /**
     * Creates a new CurrentUserChannelInterceptor object.
     *
     * @param authenticationService JAVADOC.
     * @param systemUserService JAVADOC.
     */
    public CurrentUserChannelInterceptor(AuthenticationService authenticationService,
        SystemUserService systemUserService) {
        Assert.notNull(authenticationService, "authenticationService is null");
        this.authenticationService = authenticationService;
        Assert.notNull(systemUserService, "systemUserService is null");
        this.systemUserService = systemUserService;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param message JAVADOC.
     * @param channel JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        UserDetails user = ContextUserAccessor.getCurrentUser();

        if (user == null) {
            String userName = systemUserService.getUsername();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Attempting to login for user:" + userName);
            }

            authenticationService.forceUserToContext(userName);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("There is a currently logged in user:" + user.getUsername());
            }
        }

        return super.preSend(message, channel);
    }
}
