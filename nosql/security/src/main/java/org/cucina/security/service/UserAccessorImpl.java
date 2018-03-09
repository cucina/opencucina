package org.cucina.security.service;

import org.cucina.security.api.CurrentUserAccessor;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

//import org.cucina.core.spring.ActiveProfilesAccessor;


/**
 * Convenience utility.
 *
 * @author vlevine
 */
@Component
public class UserAccessorImpl
		implements UserAccessor {
	private static final Logger LOG = LoggerFactory.getLogger(UserAccessorImpl.class);

	/*    @Autowired
		private ActiveProfilesAccessor activeProfilesAccessor;
	*/
	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @return JAVADOC.
	 * @see org.cucina.security.service.UserAccessor#getCurrentUser()
	 */
	@Override
	public User getCurrentUser() {
		Authentication auth = CurrentUserAccessor.currentAuthentication();

		if (auth == null) {
			return null;
		}

		Object principal = auth.getPrincipal();

		if (!(principal instanceof User)) {
			LOG.debug("Non-user found in security context: " + principal);

			return null;
		}

		try {
			// Get the id of the currently logged-in user.
			String name = ((User) principal).getUsername();

			// Reload the user - normally this should only involve cache access, rather than
			// a database call.
			return userRepository.findByUsername(name);
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
            /*            boolean ret = Arrays.asList(activeProfilesAccessor.getActiveProfiles())
                                            .contains(activeProfilesAccessor.getSsoProfileKey());

                        if (LOG.isInfoEnabled()) {
                            LOG.info((ret ? "SSO mode" : "Not SSO mode"));
                        }

                        return ret;

            */
		} catch (IllegalArgumentException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Details:", e);
			} else {
				LOG.info(e.getMessage());
			}
		}

		return false;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param username JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public User forceUserToContext(String username) {
		User user = userRepository.findByUsername(username);

		putToContext(user, null);

		return user;
	}

	/**
	 * JAVADOC Method Level Comments
	 *
	 * @param username JAVADOC.
	 * @param password JAVADOC.
	 * @return JAVADOC.
	 */
	@Override
	public User forceUserToContext(String username, String password) {
		User user = userRepository.findByUsername(username);

		// TODO verify password
		putToContext(user, password);

		return user;
	}

	private void putToContext(User user, String token) {
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
