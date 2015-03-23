package org.cucina.security.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.cucina.core.spring.ActiveProfilesAccessor;

import org.cucina.security.api.CurrentUserAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Convenience utility.
 *
 * @author vlevine
 */
public class UserAccessorImpl
    implements UserAccessor {
    private static final Logger LOG = LoggerFactory.getLogger(UserAccessorImpl.class);
    @Autowired
    private ActiveProfilesAccessor activeProfilesAccessor;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
    @see org.cucina.security.service.UserAccessor#getCurrentUser()     */
    @Override
    public UserDetails getCurrentUser() {
        Authentication auth = CurrentUserAccessor.currentAuthentication();

        if (auth == null) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (!(principal instanceof UserDetails)) {
            LOG.debug("Non-user found in security context: " + principal);

            return null;
        }

        try {
            // Get the id of the currently logged-in user.
            String name = ((UserDetails) principal).getUsername();

            // Reload the user - normally this should only involve cache access, rather than
            // a database call.
            return userDetailsService.loadUserByUsername(name);
        } catch (IllegalArgumentException e) {
            LOG.info(e.getMessage());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Details:", e);
            }

            return null;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    @Override
    public boolean isSso() {
        try {
            boolean ret = Arrays.asList(activeProfilesAccessor.getActiveProfiles())
                                .contains(activeProfilesAccessor.getSsoProfileKey());

            if (LOG.isInfoEnabled()) {
                LOG.info((ret ? "SSO mode" : "Not SSO mode"));
            }

            return ret;
        } catch (IllegalArgumentException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Details:", e);
            } else {
                LOG.info(e.getMessage());
            }

            return false;
        }
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public UserDetails forceUserToContext(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);

        putToContext(user, null);

        return user;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param username JAVADOC.
     * @param password JAVADOC.
     *
     * @return JAVADOC.
     */
    @Override
    public UserDetails forceUserToContext(String username, String password) {
        UserDetails user = userDetailsService.loadUserByUsername(username);

        // TODO verify password
        putToContext(user, password);

        return user;
    }

    private void putToContext(UserDetails user, String token) {
        SecurityContext context;

        if (null == SecurityContextHolder.getContext()) {
            context = new SecurityContextImpl();

            SecurityContextHolder.setContext(context);
        }

        context = SecurityContextHolder.getContext();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,
                null, null);

        authToken.setDetails(token);
        context.setAuthentication(authToken);
    }
}
