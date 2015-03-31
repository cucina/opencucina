package org.cucina.security.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.security.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
//import org.cucina.core.spring.ActiveProfilesAccessor;
import org.cucina.security.model.User;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UserAccessorImplTest {
/*    @Mock
    private ActiveProfilesAccessor activeProfilesAccessor;
*/    private User user;
    private UserAccessorImpl accessor;
    @Mock
    private UserRepository userRepository;
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
        accessor = new UserAccessorImpl();
        //ReflectionTestUtils.setField(accessor, "activeProfilesAccessor", activeProfilesAccessor);
        ReflectionTestUtils.setField(accessor, "userRepository", userRepository);
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
        assertNull(accessor.getCurrentUser());

        setSecurityContext();

        when(userRepository.findByUsername("User")).thenReturn(user);

        UserDetails found = accessor.getCurrentUser();

        assertNotNull("No user found", found);
        assertEquals(user, found);
        verify(userRepository).findByUsername("User");
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

        assertNull(accessor.getCurrentUser());
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

        assertNull(accessor.getCurrentUser());
    }

    /**
     * JAVADOC Method Level Comments
     */
   /* @Test
    public void testIsSso() {
        when(activeProfilesAccessor.getActiveProfiles()).thenReturn(new String[] {  });
        assertFalse(accessor.isSso());

        String ssoOk = "SSO-OK";

        activeProfiles = new String[] { ssoOk, "blaah" };

        when(activeProfilesAccessor.getActiveProfiles()).thenReturn(activeProfiles);
        when(activeProfilesAccessor.getSsoProfileKey()).thenReturn(ssoOk);

        assertTrue(accessor.isSso());

        when(activeProfilesAccessor.getActiveProfiles()).thenReturn(new String[] {  });

        assertFalse("Should not be sso", accessor.isSso());

        activeProfiles = null;

        assertFalse(accessor.isSso());
    }*/

    private void setSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();

        user = new User();
        user.setUsername("User");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                null, null);

        context.setAuthentication(authToken);
    }
}
