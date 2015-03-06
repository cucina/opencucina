package org.cucina.security;

import java.lang.reflect.Method;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.security.authentication.AuthenticationService;
import org.cucina.security.model.User;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.anyString;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class CurrentUserSetAdviceTest {
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private BeanFactory bf;
    @Mock
    private SystemUserService systemUserService;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        clearSecurity();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Throwable JAVADOC.
     */
    @Test
    public void testInvokeInContext()
        throws Throwable {
        CurrentUserSetAdvice interceptor = new CurrentUserSetAdvice(authenticationService,
                systemUserService);

        when(systemUserService.getUsername()).thenReturn("userName");

        Method method = this.getClass().getMethod("setup", (Class<?>[]) null);

        interceptor.before(method, null, null);

        verify(authenticationService).forceUserToContext("userName");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Throwable JAVADOC.
     */
    @Test
    public void testWithCurrentAuth()
        throws Throwable {
        setSecurity();

        CurrentUserSetAdvice interceptor = new CurrentUserSetAdvice(authenticationService,
                systemUserService);

        interceptor.before(null, null, null);
        verify(systemUserService, never()).getUsername();
        verify(authenticationService, never()).forceUserToContext(anyString());
    }

    private void setSecurity() {
        SecurityContext context = SecurityContextHolder.getContext();
        User user = new User();

        user.setUsername("User");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                null, null);

        authToken.setDetails("detailsToken");
        context.setAuthentication(authToken);

        final UserDetailsService userAccessor = mock(UserDetailsService.class);

        when(userAccessor.loadUserByUsername(user.getUsername())).thenReturn(user);
        when(bf.getBean(ContextUserAccessor.USER_ACCESSOR_ID)).thenReturn(userAccessor);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bf);
    }

    private void clearSecurity() {
        SecurityContext context = SecurityContextHolder.getContext();

        context.setAuthentication(null);
    }
}
