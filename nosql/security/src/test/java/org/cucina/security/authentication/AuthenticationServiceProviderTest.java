package org.cucina.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.cucina.security.model.User;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AuthenticationServiceProviderTest {
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UsernamePasswordAuthenticationToken authenticationToken;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testAdditionalAuthenticationChecks() {
        AuthenticationServiceProvider provider = new AuthenticationServiceProvider(authenticationService);

        when(authenticationToken.getName()).thenReturn("user");
        when(authenticationToken.getCredentials()).thenReturn("password");

        when(authenticationService.authenticate("user", "password")).thenReturn("token");

        User userDetails = new User();

        provider.additionalAuthenticationChecks(userDetails, authenticationToken);
        verify(authenticationToken).setDetails("token");
    }
}
