package org.cucina.security.testassist;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;


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
	}
}
