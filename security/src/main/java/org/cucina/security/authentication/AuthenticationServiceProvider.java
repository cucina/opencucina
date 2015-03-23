package org.cucina.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;


/**
 * Extension of spring DaoAuthenticationProvider that authenticates with
 * authentication service.
 *
 * @author $Author: $
 * @version $Revision: $
 */
public class AuthenticationServiceProvider
    extends DaoAuthenticationProvider {
    private AuthenticationService authenticationService;

    /**
     * Creates a new AuthenticationServiceProvider object.
     *
     * @param authenticationService JAVADOC.
     */
    public AuthenticationServiceProvider(AuthenticationService authenticationService) {
        Assert.notNull(authenticationService, "authenticationService is null");
        this.authenticationService = authenticationService;
    }

    /**
     * Authenticate with the authentication service and populate the details
     * with token returned from the authentication service.
     *
     * @param userDetails
     *            UserDetails.
     * @param authenticationToken
     *            UsernamePasswordAuthenticationToken.
     *
     * @throws AuthenticationException.
     * @throws GrapefruitAuthenticationException.
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
        UsernamePasswordAuthenticationToken authenticationToken)
        throws AuthenticationException {
        // If ok set details as token
        authenticationToken.setDetails(authenticationService.authenticate(
                authenticationToken.getName(), (String) authenticationToken.getCredentials()));
    }
}
