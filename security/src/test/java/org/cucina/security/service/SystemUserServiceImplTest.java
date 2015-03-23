package org.cucina.security.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.cucina.security.service.SystemUserServiceImpl;
import org.cucina.security.service.UserPasswordSetter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class SystemUserServiceImplTest {
    private SystemUserServiceImpl systemUserServiceImpl;
    @Mock
    private UserPasswordSetter userPasswordSetter;
    @Mock
    private UserRepository userRepository;

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        systemUserServiceImpl = new SystemUserServiceImpl(userRepository);
    }

    /**
     * If a username and password are provided then an admin user is created.
     *
     * @throws Exception.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetUserName()
        throws Exception {
        systemUserServiceImpl.setUsername("mike");
        systemUserServiceImpl.setPassword("fred");
        systemUserServiceImpl.setUserPasswordSetter(userPasswordSetter);
        systemUserServiceImpl.afterPropertiesSet();
        when(userRepository.loadUserByUsername("mike")).thenThrow(UsernameNotFoundException.class);
        systemUserServiceImpl.getUsername();
        verify(userPasswordSetter).createAdminUser("mike", "fred");
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    public void testGetUserNameOutOfTheBlue()
        throws Exception {
        User user = new User();

        user.setName("mike");

        Collection<User> users = Collections.singleton(user);

        when(userRepository.findBySystem(true)).thenReturn(users);
        assertEquals("mike", systemUserServiceImpl.getUsername());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Throwable JAVADOC.
     */
    @Test
    public void testInvokeSearch()
        throws Throwable {
        String systemUserName = "luserName";
        User user = new User();

        user.setUsername(systemUserName);
        when(userRepository.findBySystem(true)).thenReturn(Collections.singleton(user));

        assertEquals(systemUserName, systemUserServiceImpl.getUsername());
        verify(userRepository, times(1)).findBySystem(true);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Throwable JAVADOC.
     */
    @Test
    public void testUsernameOverridesSystemUserCall()
        throws Throwable {
        String userName = "USER";

        systemUserServiceImpl.setUsername(userName);

        assertEquals(userName, systemUserServiceImpl.getUsername());
        verify(userRepository, never()).findBySystem(true);
    }
}
