package org.cucina.security.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * JAVADOC for Class Level
 *
 * @author vlevine
 */
public abstract class CurrentUserAccessor {
	private static final Logger LOG = LoggerFactory.getLogger(CurrentUserAccessor.class);

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
	 * @return JAVADOC.
	 */
	public static Authentication currentAuthentication() {
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

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param replacement JAVADOC.
	 * @return JAVADOC.
	 */
	public static Authentication switchAuthentication(Authentication replacement) {
		Authentication original = currentAuthentication();

		SecurityContext context = SecurityContextHolder.getContext();

		context.setAuthentication(replacement);

		return original;
	}
}
