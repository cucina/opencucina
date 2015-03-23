package org.cucina.security.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import org.aopalliance.intercept.MethodInvocation;

import org.cucina.security.api.CurrentUserAccessor;
import org.cucina.security.model.User;
import static org.junit.Assert.assertEquals;

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
public class SystemUserMethodInterceptorTest {
    @Mock
    private BeanFactory beanFactory;
    @Mock
    private MethodInvocation methodInvocation;
    @Mock
    private SystemUserService systemUserService;
    @Mock
    private UserAccessor userAccessor;
    @Mock
    private UserDetailsService userDetailsService;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        clearSecurity();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Throwable JAVADOC.
     */
    @Test
    public void test()
        throws Throwable {
        //create authentication
        User user = new User();

        user.setUsername("loggedin");

        //set security
        AbstractAuthenticationToken authToken = setSecurity(user, true);

        //mock systemUserService returns username
        String systemUsername = "ADMIN";

        when(systemUserService.getUsername()).thenReturn(systemUsername);

        SystemUserMethodInterceptor interceptor = new SystemUserMethodInterceptor(userAccessor,
                systemUserService);

        interceptor.invoke(methodInvocation);
        //mock authenticatioNService call
        verify(userAccessor).forceUserToContext(systemUsername);
        verify(methodInvocation).proceed();

        //test it switches back
        assertEquals(CurrentUserAccessor.currentAuthentication(), authToken);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Throwable JAVADOC.
     */
    @Test
    public void testWithNull()
        throws Throwable {
        if (null == SecurityContextHolder.getContext()) {
            SecurityContextHolder.setContext(new SecurityContextImpl());
        }

        //mock systemUserService returns username
        String systemUsername = "ADMIN";

        when(systemUserService.getUsername()).thenReturn(systemUsername);

        SystemUserMethodInterceptor interceptor = new SystemUserMethodInterceptor(userAccessor,
                systemUserService);

        interceptor.invoke(methodInvocation);
        //mock authenticatioNService call
        verify(userAccessor).forceUserToContext(systemUsername);
        verify(methodInvocation).proceed();

        //test it switches back
        assertEquals(CurrentUserAccessor.currentAuthentication(), null);
    }

    private AbstractAuthenticationToken setSecurity(User user, boolean isPreAuth) {
        if (null == SecurityContextHolder.getContext()) {
            SecurityContextHolder.setContext(new SecurityContextImpl());
        }

        SecurityContext context = SecurityContextHolder.getContext();

        AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null);

        if (isPreAuth) {
            authToken = new PreAuthenticatedAuthenticationToken(user, null);
        }

        authToken.setDetails("pipipi");
        context.setAuthentication(authToken);

        return authToken;
    }

    private void clearSecurity() {
        SecurityContext context = SecurityContextHolder.getContext();

        context.setAuthentication(null);
    }
}
