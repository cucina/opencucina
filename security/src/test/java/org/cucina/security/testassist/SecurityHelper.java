package org.cucina.security.testassist;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.cucina.core.spring.SingletonBeanFactory;
import org.cucina.security.ContextUserAccessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * Helper class related to security within sprite
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SecurityHelper {
    /**
     * Sets up the security with User and number of times they will be loaded.
     *
     * @param user SpriteUser.
     */
    public static void setupSecurity(UserDetails user) {
        if (null == SecurityContextHolder.getContext()) {
            SecurityContextHolder.setContext(new SecurityContextImpl());
        }

        SecurityContext context = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                null);

        authToken.setDetails("pipipi");
        context.setAuthentication(authToken);

        final UserDetailsService userAccesor = mock(UserDetailsService.class);

        when(userAccesor.loadUserByUsername(user.getUsername())).thenReturn(user);

        BeanFactory bf = mock(BeanFactory.class);

        when(bf.getBean(ContextUserAccessor.USER_ACCESSOR_ID)).thenReturn(userAccesor);
        ((SingletonBeanFactory) SingletonBeanFactory.getInstance()).setBeanFactory(bf);
    }
}
