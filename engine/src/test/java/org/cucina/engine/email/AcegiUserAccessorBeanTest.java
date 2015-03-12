package org.cucina.engine.email;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.cucina.core.spring.SingletonBeanFactory;

import org.cucina.security.ContextUserAccessor;
import org.cucina.security.model.User;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Test that AcegiengineUserAccessor functions as expected
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AcegiUserAccessorBeanTest {
    private AcegiUserAccessorBean engineUserAccessor;
    private User user;
    private UserDetailsService userAccesor;

    /**
     * Set up for test
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        userAccesor = mock(UserDetailsService.class);

        BeanFactory bf = mock(BeanFactory.class);

        when(bf.getBean(ContextUserAccessor.USER_ACCESSOR_ID)).thenReturn(userAccesor);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bf);

        engineUserAccessor = new AcegiUserAccessorBean();
    }

    /**
     * Test current User is returned from Acegi
     */
    @Test
    public void testGetCurrentUser() {
        setSecurityContext();

        when(userAccesor.loadUserByUsername("User")).thenReturn(user);

        Object found = engineUserAccessor.getCurrentUser();

        assertNotNull("No user found", found);
        assertEquals(user, found);
    }

    private void setSecurityContext() {
        SecurityContext context = null;

        if (null == SecurityContextHolder.getContext()) {
            context = new SecurityContextImpl();

            SecurityContextHolder.setContext(context);
        }

        context = SecurityContextHolder.getContext();

        user = new User();
        user.setId(new Long(1));
        user.setUsername("User");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                null, null);

        context.setAuthentication(authToken);
    }
}
