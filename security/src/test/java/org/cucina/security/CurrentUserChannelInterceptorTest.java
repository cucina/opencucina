
package org.cucina.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.security.authentication.AuthenticationService;
import org.cucina.security.model.User;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CurrentUserChannelInterceptorTest {
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private BeanFactory beanFactory;
    private CurrentUserChannelInterceptor interceptor;
    @Mock
    private Message<?> message;
    @Mock
    private MessageChannel messageChannel;
    @Mock
    private SystemUserService systemUserService;
    private User user;
    @Mock
    private UserDetailsService userAccessor;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        LoggingEnabler.enableLog(CurrentUserChannelInterceptor.class);
        MockitoAnnotations.initMocks(this);
        interceptor = new CurrentUserChannelInterceptor(authenticationService, systemUserService);

        if (null == SecurityContextHolder.getContext()) {
            SecurityContextHolder.setContext(new SecurityContextImpl());
        }

        SecurityContext context = SecurityContextHolder.getContext();

        user = new User();
        user.setName("user");

        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null);

        authToken.setDetails("pipipi");
        context.setAuthentication(authToken);
        when(beanFactory.getBean(ContextUserAccessor.USER_ACCESSOR_ID)).thenReturn(userAccessor);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testCurrentUser() {
        when(userAccessor.loadUserByUsername(user.getUsername())).thenReturn(user);
        interceptor.preSend(message, messageChannel);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoCurrentUser() {
        when(systemUserService.getUsername()).thenReturn("system");
        when(userAccessor.loadUserByUsername(user.getUsername())).thenReturn(null);
        interceptor.preSend(message, messageChannel);
        verify(authenticationService).forceUserToContext("system");
    }
}
