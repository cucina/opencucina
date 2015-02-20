
package org.cucina.security.authentication.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class RequestHeaderRedirectAuthenticationFilterTest {
    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void alternativeHeaderNameIsSupported()
        throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("myUsernameHeader", "wolfman");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        RequestHeaderRedirectProcessingFilter filter = new RequestHeaderRedirectProcessingFilter(
                "/notused");

        filter.setAuthenticationManager(createAuthenticationManager());
        filter.setPrincipalRequestHeader("myUsernameHeader");

        filter.doFilter(request, response, chain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("wolfman", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @After
    @Before
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void credentialsAreRetrievedIfHeaderNameIsSet()
        throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setSession(new MockHttpSession());

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        RequestHeaderRedirectProcessingFilter filter = new RequestHeaderRedirectProcessingFilter(
                "/notused");

        filter.setAuthenticationManager(createAuthenticationManager());
        filter.setCredentialsRequestHeader("myCredentialsHeader");
        request.addHeader("SM_USER", "cat");
        request.addHeader("myCredentialsHeader", "catspassword");

        filter.doFilter(request, response, chain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("catspassword",
            SecurityContextHolder.getContext().getAuthentication().getCredentials());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    public void defaultsToUsingSiteminderHeader()
        throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.addHeader("SM_USER", "cat");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        RequestHeaderRedirectProcessingFilter filter = new RequestHeaderRedirectProcessingFilter(
                "/notused");

        filter.setAuthenticationManager(createAuthenticationManager());

        filter.doFilter(request, response, chain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("cat", SecurityContextHolder.getContext().getAuthentication().getName());
        assertEquals("N/A", SecurityContextHolder.getContext().getAuthentication().getCredentials());
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Before
    public void logging() {
        LoggingEnabler.enableLog(RequestHeaderRedirectProcessingFilter.class);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     * @throws TestException JAVADOC.
     */
    @Test(expected = TestException.class)
    public void missingHeaderCausesFailureHandler()
        throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        RequestHeaderRedirectProcessingFilter filter = new RequestHeaderRedirectProcessingFilter(
                "/notused");

        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request,
                    HttpServletResponse response, AuthenticationException exception)
                    throws IOException, ServletException {
                    throw new TestException("failed");
                }
            });
        filter.setAuthenticationManager(createAuthenticationManager());

        filter.doFilter(request, response, chain);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void missingHeaderIsIgnoredIfExceptionIfHeaderMissingIsFalse()
        throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        RequestHeaderRedirectProcessingFilter filter = new RequestHeaderRedirectProcessingFilter(
                "/notused");

        filter.setExceptionIfHeaderMissing(false);
        filter.setAuthenticationManager(createAuthenticationManager());
        filter.doFilter(request, response, chain);
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        RequestHeaderRedirectProcessingFilter filter = new RequestHeaderRedirectProcessingFilter(
                "**");

        filter.afterPropertiesSet();
    }

    /**
     * JAVADOC Method Level Comments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullConstructorArg() {
        RequestHeaderRedirectProcessingFilter filter = new RequestHeaderRedirectProcessingFilter(null);

        filter.afterPropertiesSet();
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void userIsReauthenticatedIfPrincipalChangesAndCheckForPrincipalChangesIsSet()
        throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        RequestHeaderRedirectProcessingFilter filter = new RequestHeaderRedirectProcessingFilter(
                "/notused");

        filter.setAuthenticationManager(createAuthenticationManager());
        filter.setCheckForPrincipalChanges(true);
        request.addHeader("SM_USER", "cat");
        filter.doFilter(request, response, new MockFilterChain());
        assertNotNull(request.getSession());
        request = new MockHttpServletRequest();
        request.addHeader("SM_USER", "dog");
        filter.doFilter(request, response, new MockFilterChain());

        Authentication dog = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(dog);
        assertEquals("dog", dog.getName());
        // Make sure authentication doesn't occur every time (i.e. if the header *doesn't change)
        filter.setAuthenticationManager(mock(AuthenticationManager.class));
        filter.doFilter(request, response, new MockFilterChain());
        assertSame(dog, SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Create an authentication manager which returns the passed in object.
     */
    private AuthenticationManager createAuthenticationManager() {
        AuthenticationManager am = mock(AuthenticationManager.class);

        when(am.authenticate(any(Authentication.class))).thenAnswer(new Answer<Authentication>() {
                public Authentication answer(InvocationOnMock invocation)
                    throws Throwable {
                    return (Authentication) invocation.getArguments()[0];
                }
            });

        return am;
    }

    public static class HttpSessionThrowsTestException
        extends MockHttpSession {
        @Override
        public void invalidate() {
            throw new TestException();
        }
    }

    public static class TestException
        extends RuntimeException {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public TestException() {
            super();
        }

        public TestException(String message, Throwable cause) {
            super(message, cause);
        }

        public TestException(String message) {
            super(message);
        }

        public TestException(Throwable cause) {
            super(cause);
        }
    }
}
