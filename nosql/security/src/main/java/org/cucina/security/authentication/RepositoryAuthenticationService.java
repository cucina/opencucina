package org.cucina.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;

/**
 *
 *
 * @author vlevine
 */
@Service
public class RepositoryAuthenticationService implements AuthenticationService {
	@Autowired
	private UserRepository userRepository;

	/**
	 *
	 *
	 * @param username
	 *            .
	 * @param password
	 *            .
	 *
	 * @return .
	 *
	 * @throws AuthenticationException .
	 * @throws UsernameNotFoundException .
	 * @throws BadCredentialsException .
	 */
	@Override
	public Authentication authenticate(String username, String password)
			throws AuthenticationException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Username '" + username
					+ "' not found");
		}

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				username, null, user.getAuthorities());

		if ((password == null) && (user.getPassword() == null)) {
			return token;
		}

		if ((password == null) || (user.getPassword() == null)) {
			throw new BadCredentialsException("Null password");
		}

		if (!password.equals(user.getPassword().getValue())) {
			throw new BadCredentialsException("Failed to match password for '"
					+ username + "'");
		}
		// TODO persistable session with login time and this token which can be
		// used for cookies etc.
		// token.setDetails(one time token);
		return token;
	}
}
