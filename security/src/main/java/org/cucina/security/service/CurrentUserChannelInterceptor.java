package org.cucina.security.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CurrentUserChannelInterceptor
    extends ChannelInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(CurrentUserChannelInterceptor.class);
    private SystemUserService systemUserService;
    private UserAccessor userAccessor;

    /**
     * Creates a new CurrentUserChannelInterceptor object.
     *
     * @param authenticationService JAVADOC.
     * @param systemUserService JAVADOC.
     */
    public CurrentUserChannelInterceptor(SystemUserService systemUserService,
        UserAccessor userAccessor) {
        Assert.notNull(systemUserService, "systemUserService is null");
        this.systemUserService = systemUserService;
        Assert.notNull(userAccessor, "userAccessor is null");
        this.userAccessor = userAccessor;
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
        UserDetails user = userAccessor.getCurrentUser();

        if (user == null) {
            String userName = systemUserService.getUsername();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Attempting to login for user:" + userName);
            }

            userAccessor.forceUserToContext(userName);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("There is a currently logged in user:" + user.getUsername());
            }
        }

        return super.preSend(message, channel);
    }
}
