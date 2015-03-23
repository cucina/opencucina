package org.cucina.security.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import org.cucina.security.model.User;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CurrentUserChannelInterceptorTest {
    private CurrentUserChannelInterceptor interceptor;
    @Mock
    private Message<?> message;
    @Mock
    private MessageChannel messageChannel;
    @Mock
    private SystemUserService systemUserService;
    private User user;
    @Mock
    private UserAccessor userAccessor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        interceptor = new CurrentUserChannelInterceptor(systemUserService, userAccessor);

        if (null == SecurityContextHolder.getContext()) {
            SecurityContextHolder.setContext(new SecurityContextImpl());
        }

        SecurityContext context = SecurityContextHolder.getContext();

        user = new User();
        user.setName("user");

        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null);

        authToken.setDetails("pipipi");
        context.setAuthentication(authToken);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testCurrentUser() {
        when(userAccessor.getCurrentUser()).thenReturn(user);
        interceptor.preSend(message, messageChannel);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoCurrentUser() {
        when(systemUserService.getUsername()).thenReturn("system");
        when(userAccessor.getCurrentUser()).thenReturn(null);
        interceptor.preSend(message, messageChannel);
        verify(userAccessor).forceUserToContext("system");
    }
}
