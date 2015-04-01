
package org.cucina.security.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.HashMap;

import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class MapBasedLoginAttemptsHandlerTest {
    private static final String USERNAME = "aUser";
    private MapBasedLoginAttemptsHandler handler;
    @Mock
    private UserRepository userRepository;

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new MapBasedLoginAttemptsHandler(userRepository, 1);
    }

    /**
     * Test that a successful login resets the attempt count.
     */
    @Test
    public void testLoginSuccess() {
        HashMap<String, Integer> failedLoginAttempts = new HashMap<String, Integer>();

        failedLoginAttempts.put(USERNAME, 1);
        ReflectionTestUtils.setField(handler, "failedLoginAttempts", failedLoginAttempts);
        handler.loginSuccess(USERNAME);
        assertNull("Should've cleared the attempt count", failedLoginAttempts.get(USERNAME));
    }

    /**
     * Test that we lock the user account after exceeding the allowed failed login attempts.
     */
    @Test
    public void testRegisterFailedAttempt() {
        User user = new User();
        user.setId(BigInteger.valueOf(1l));
        user.setAccountNonLocked(true);
        user.setUsername(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        handler.registerFailedAttempt(USERNAME);
        assertTrue("Shouldn't have locked the account yet", user.isAccountNonLocked());
        handler.registerFailedAttempt(USERNAME);
        assertFalse("Account should now have been locked", user.isAccountNonLocked());
        verify(userRepository).save(user);
    }
}
