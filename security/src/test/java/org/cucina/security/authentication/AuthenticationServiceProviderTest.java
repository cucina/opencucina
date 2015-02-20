
package org.cucina.security.authentication;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.security.model.User;
import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


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
        LoggingEnabler.enableLog(AuthenticationServiceProvider.class);
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
