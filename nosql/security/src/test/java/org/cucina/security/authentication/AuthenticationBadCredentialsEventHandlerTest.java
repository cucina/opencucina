
package org.cucina.security.authentication;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.security.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class AuthenticationBadCredentialsEventHandlerTest {
    private AuthenticationBadCredentialsEventHandler handler;
    @Mock
    private LoginAttemptsHandler loginAttemptsHandler;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new AuthenticationBadCredentialsEventHandler();
        handler.setLoginAttemptsHandler(loginAttemptsHandler);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = IllegalArgumentException.class)
    public void rainyDay() {
        Authentication auth = mock(Authentication.class);
        User user = new User();

        user.setUsername("bla");

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(auth,
                new BadCredentialsException("someone bad"));

        when(auth.getPrincipal()).thenReturn(user);
        handler.onApplicationEvent(event);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void testSunnyDay() {
        Authentication auth = mock(Authentication.class);
        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(auth,
                new BadCredentialsException("someone bad"));

        when(auth.getPrincipal()).thenReturn("aUser");
        handler.onApplicationEvent(event);
        verify(loginAttemptsHandler).registerFailedAttempt("aUser");
    }
}
