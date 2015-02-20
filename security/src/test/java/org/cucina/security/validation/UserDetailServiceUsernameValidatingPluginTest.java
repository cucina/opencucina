package org.cucina.security.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cucina.security.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * Tests that UserDaoUsernameValidatingPlugin functions as expected.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UserDetailServiceUsernameValidatingPluginTest {
    private static final String USERNAME = "username";
    private User user;
    private UserDetailServiceUsernameValidatingPlugin plugin;
    @Mock
    private UserDetailsService userDetailsService;

    /**
     * Message is incorrect.
     */
    @Test
    public void message() {
        assertEquals("Should have correct message",
            "{org.cucina.meringue.security.validation.ValidUsername.unique}", plugin.message());
    }

    /**
     * Sets up for test
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        plugin = new UserDetailServiceUsernameValidatingPlugin(userDetailsService);

        user = new User();
    }

    /**
     * User not found exception confirms username is valid
     */
    @Test
    public void userNotFoundException() {
        when(userDetailsService.loadUserByUsername(USERNAME))
            .thenThrow(new UsernameNotFoundException(USERNAME));
        assertTrue("Should be found as no user", plugin.isValid(USERNAME));

        verify(userDetailsService).loadUserByUsername(USERNAME);
    }

    /**
     * Username is invalid if user is found.
     */
    @Test
    public void usernameFound() {
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(user);

        assertFalse("Should not be valid as user is found", plugin.isValid(USERNAME));
    }

    /**
     * Username is valid if no user is found
     */
    @Test
    public void usernameNotFound() {
        assertTrue("Should be found as no user", plugin.isValid(USERNAME));
    }
}
