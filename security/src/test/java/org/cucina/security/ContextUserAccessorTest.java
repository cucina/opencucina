package org.cucina.security;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.cucina.core.spring.ActiveProfilesAccessor;
import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.security.model.User;
import org.cucina.security.testassist.Foo;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
public class ContextUserAccessorTest {
    @Mock
    private ActiveProfilesAccessor activeProfilesAccessor;
    @Mock
    private BeanFactory beanFactory;
    private User user;
    @Mock
    private UserDetailsService userAccesor;
    private String[] activeProfiles;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        when(beanFactory.getBean(ContextUserAccessor.USER_ACCESSOR_ID)).thenReturn(userAccesor);
        when(beanFactory.getBean(ActiveProfilesAccessor.PROFILES_ACCESSOR_ID))
            .thenReturn(activeProfilesAccessor);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(beanFactory);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @After
    public void tearDown() {
        SecurityContext context = null;

        if (null == SecurityContextHolder.getContext()) {
            context = new SecurityContextImpl();

            SecurityContextHolder.setContext(context);
        }

        context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetCurrentUser() {
        SecurityContextHolder.clearContext();
        assertNull(ContextUserAccessor.getCurrentUser());

        setSecurityContext();

        when(userAccesor.loadUserByUsername("User")).thenReturn(user);

        UserDetails found = ContextUserAccessor.getCurrentUser();

        assertNotNull("No user found", found);
        assertEquals(user, found);
        verify(userAccesor).loadUserByUsername("User");
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetCurrentUserNoUserDao() {
        SecurityContext context = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(new User(),
                null, null);

        context.setAuthentication(authToken);

        assertNull(ContextUserAccessor.getCurrentUser());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testGetCurrentUserWithWeirdPrincipal() {
        SecurityContext context = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(new Object(),
                null, null);

        context.setAuthentication(authToken);

        assertNull(ContextUserAccessor.getCurrentUser());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsSso() {
        when(activeProfilesAccessor.getActiveProfiles()).thenReturn(new String[] {  });
        assertFalse(ContextUserAccessor.isSso());

        String ssoOk = "SSO-OK";

        activeProfiles = new String[] { ssoOk, "blaah" };

        when(activeProfilesAccessor.getActiveProfiles()).thenReturn(activeProfiles);
        when(activeProfilesAccessor.getSsoProfileKey()).thenReturn(ssoOk);

        assertTrue(ContextUserAccessor.isSso());

        when(activeProfilesAccessor.getActiveProfiles()).thenReturn(new String[] {  });

        assertFalse("Should not be sso", ContextUserAccessor.isSso());

        activeProfiles = null;

        assertFalse(ContextUserAccessor.isSso());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testIsUserAuthenticated() {
        assertFalse("Oops, there should not be an authenticated user!",
            ContextUserAccessor.isUserAuthenticated());
        setSecurityContext();
        assertTrue("Oops, there is no authenticated user!",
            ContextUserAccessor.isUserAuthenticated());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testNoBarfWhenUserDetailsAreNotString() {
        SecurityContextHolder.clearContext();
        assertNull(ContextUserAccessor.getCurrentUserDetails());

        setSecurityContext();

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
                                                                                                              .getAuthentication();
        String details = "blaah";

        auth.setDetails(details);
        assertEquals(details, ContextUserAccessor.getCurrentUserDetails());

        Foo foo = new Foo();

        auth.setDetails(foo);
        assertEquals(foo, ContextUserAccessor.getCurrentUserDetails());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void userName() {
        SecurityContextHolder.clearContext();
        assertNull(ContextUserAccessor.getCurrentUserName());

        SecurityContext context = new SecurityContextImpl();

        SecurityContextHolder.setContext(context);
        assertNull(ContextUserAccessor.getCurrentUserName());
        setSecurityContext();
        assertEquals("User", ContextUserAccessor.getCurrentUserName());
    }

    private void setSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();

        user = new User();
        user.setUsername("User");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                null, null);

        context.setAuthentication(authToken);
    }
}
