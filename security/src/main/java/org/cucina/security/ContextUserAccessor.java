package org.cucina.security;

import java.util.Arrays;

import org.cucina.core.spring.ActiveProfilesAccessor;
import org.cucina.core.spring.SingletonBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;


/**
 * Convenience utility.
 *
 * @author vlevine
 */
public abstract class ContextUserAccessor {
    private static final Logger LOG = LoggerFactory.getLogger(ContextUserAccessor.class);

    /** This is a field JAVADOC */
    public static final String USER_ACCESSOR_ID = "userDetailsService";

    /**
     * Uses the persistence service to obtain the most up-to-date version of
     * the currently logged-in user.
     *
     * @return JAVADOC.
     */
    public static UserDetails getCurrentUser() {
        Authentication auth = currentAuthentication();

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

            // Get a reference to the persistence service.
            UserDetailsService psm = (UserDetailsService) SingletonBeanFactory.getInstance()
                                                                              .getBean(ContextUserAccessor.USER_ACCESSOR_ID);

            Assert.notNull(psm,
                "Failed to find a bean '" + ContextUserAccessor.USER_ACCESSOR_ID + "'");

            // Reload the user - normally this should only involve cache access, rather than
            // a database call.
            return psm.loadUserByUsername(name);
        } catch (IllegalArgumentException e) {
            LOG.info(e.getMessage());

            if (LOG.isDebugEnabled()) {
                LOG.debug("Details:", e);
            }

            return null;
        }
    }

    /**
     * Access current authentication and gets its details
     *
     * @return Auth's details object or null if either .
     */
    public static Object getCurrentUserDetails() {
        Authentication auth = currentAuthentication();

        if (auth == null) {
            return null;
        }

        return auth.getDetails();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public static String getCurrentUserName() {
        Authentication auth = currentAuthentication();

        if (auth == null) {
            return null;
        }

        return auth.getName();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    public static boolean isSso() {
        try {
            ActiveProfilesAccessor accessor = (ActiveProfilesAccessor) SingletonBeanFactory.getInstance()
                                                                                           .getBean(ActiveProfilesAccessor.PROFILES_ACCESSOR_ID);

            Assert.notNull(accessor, "Accessor is null");

            boolean ret = Arrays.asList(accessor.getActiveProfiles())
                                .contains(accessor.getSsoProfileKey());

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
     * JAVADOC.
     *
     * @return JAVADOC.
     */
    public static boolean isUserAuthenticated() {
        Object principal = null;

        Authentication auth = currentAuthentication();

        if (auth == null) {
            return false;
        }

        principal = auth.getPrincipal();

        if (LOG.isDebugEnabled()) {
            LOG.debug("authenticated =" +
                ((principal != null) && principal instanceof UserDetails));

            if (principal instanceof UserDetails) {
                UserDetails user = (UserDetails) principal;

                LOG.debug("Username= [" + user.getUsername() + "]");
            }
        }

        return (principal != null) && principal instanceof UserDetails;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @param replacement JAVADOC.
     *
     * @return JAVADOC.
     */
    public static Authentication switchAuthentication(Authentication replacement) {
        Authentication original = currentAuthentication();

        SecurityContext context = SecurityContextHolder.getContext();

        context.setAuthentication(replacement);

        return original;
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @return JAVADOC.
     */
    static Authentication currentAuthentication() {
        // current implementations never return an empty context
        SecurityContext context = SecurityContextHolder.getContext();

        Authentication authentication = context.getAuthentication();

        if (null == authentication) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Authentication is null, returning null user");
            }
        }

        return authentication;
    }
}
