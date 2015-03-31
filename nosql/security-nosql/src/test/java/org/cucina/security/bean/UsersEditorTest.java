package org.cucina.security.bean;

import java.math.BigInteger;

import java.util.Collection;

import org.springframework.dao.OptimisticLockingFailureException;

import org.cucina.security.model.Role;
import org.cucina.security.model.User;
import org.cucina.security.repository.RoleRepository;
import org.cucina.security.repository.UserRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class UsersEditorTest {
    private static final String USER1_NAME = "user1";
    private static final String USER2_NAME = "user2";
    private User user1;
    private User user2;
    @Mock
    private UserRepository userRepository;

    /**
     * If empty text is provided does nothing
     */
    @Test
    public void emptyText() {
        UsersEditor editor = new UsersEditor(userRepository);

        editor.setAsText("");

        @SuppressWarnings("unchecked")
        Collection<Role> roles = (Collection<Role>) editor.getValue();

        assertNotNull("Should return collection of roles", roles);
        assertEquals("Incorrect number roles", 0, roles.size());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsUsers() {
        when(userRepository.findByUsername(USER1_NAME)).thenReturn(user1);
        when(userRepository.findByUsername(USER2_NAME)).thenReturn(user2);

        UsersEditor editor = new UsersEditor(userRepository);

        editor.setAsText(USER1_NAME + "," + USER2_NAME);

        @SuppressWarnings("unchecked")
        Collection<User> users = (Collection<User>) editor.getValue();

        assertNotNull("Should return collection of users", users);
        assertEquals("Incorrect number users", 2, users.size());
        assertTrue("Should contain user1", users.contains(user1));
        assertTrue("Should contain user2", users.contains(user2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test
    public void loadsUsersWithWhitespace() {
        when(userRepository.findByUsername(USER1_NAME)).thenReturn(user1);
        when(userRepository.findByUsername(USER2_NAME)).thenReturn(user2);

        UsersEditor editor = new UsersEditor(userRepository);

        editor.setAsText(USER1_NAME + " , " + USER2_NAME);

        @SuppressWarnings("unchecked")
        Collection<Role> roles = (Collection<Role>) editor.getValue();

        assertNotNull("Should return collection of roles", roles);
        assertEquals("Incorrect number roles", 2, roles.size());
        assertTrue("Should contain role1", roles.contains(user1));
        assertTrue("Should contain role2", roles.contains(user2));
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void onsetup() {
        MockitoAnnotations.initMocks(this);
        user1 = new User();
        user1.setId(BigInteger.valueOf(11L));
        user1.setName(USER1_NAME);

        user2 = new User();
        user2.setId(BigInteger.valueOf(12L));
        user2.setName(USER2_NAME);
    }

    /**
     * If
     */
    @Test(expected = OptimisticLockingFailureException.class)
    public void userNoLongerExists() {
        RoleRepository rr = mock(RoleRepository.class);

        when(rr.findByName(USER1_NAME)).thenReturn(null);

        UsersEditor editor = new UsersEditor(userRepository);

        editor.setAsText(USER1_NAME + "," + USER2_NAME);
    }
}
