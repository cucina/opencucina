
package org.cucina.security.authentication.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;

import org.cucina.testassist.utils.LoggingEnabler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;


/**
 * JAVADOC for Class Level
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class ExceptionMappingMessageCodeFailureHandlerTest {
    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Before
    public void setUp()
        throws Exception {
        LoggingEnabler.enableLog(ExceptionMappingMessageCodeFailureHandler.class);
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void defaultMessageCodeIsUsedIfNoMappingExists()
        throws Exception {
        ExceptionMappingMessageCodeFailureHandler fh = new ExceptionMappingMessageCodeFailureHandler();

        fh.setDefaultFailureUrl("/failed");
        fh.setDefaultMessageCode("DEFAULT");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();

        fh.onAuthenticationFailure(request, response, new BadCredentialsException(""));
        assertEquals("DEFAULT",
            request.getSession()
                   .getAttribute(ExceptionMappingMessageCodeFailureHandler.AUTHENTICATION_MESSAGE_CODE_KEY));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void error401IsReturnedIfNoUrlIsSet()
        throws Exception {
        ExceptionMappingMessageCodeFailureHandler afh = new ExceptionMappingMessageCodeFailureHandler();

        RedirectStrategy rs = mock(RedirectStrategy.class);

        afh.setRedirectStrategy(rs);

        assertSame(rs, afh.getRedirectStrategy());

        MockHttpServletRequest request = new MockHttpServletRequest();

        MockHttpServletResponse response = new MockHttpServletResponse();

        afh.onAuthenticationFailure(request, response, mock(AuthenticationException.class));
        assertEquals(401, response.getStatus());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void exceptionIsNotSavedIfAllowSessionCreationIsFalse()
        throws Exception {
        ExceptionMappingMessageCodeFailureHandler afh = new ExceptionMappingMessageCodeFailureHandler();

        afh.setAllowSessionCreation(false);
        assertFalse(afh.isAllowSessionCreation());

        MockHttpServletRequest request = new MockHttpServletRequest();

        MockHttpServletResponse response = new MockHttpServletResponse();

        afh.onAuthenticationFailure(request, response, mock(AuthenticationException.class));

        assertNull(request.getSession(false));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void exceptionIsSavedToSessionOnRedirect()
        throws Exception {
        ExceptionMappingMessageCodeFailureHandler afh = new ExceptionMappingMessageCodeFailureHandler("/target",
                "code");

        afh.setDefaultFailureUrl("/target");

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException e = mock(AuthenticationException.class);

        afh.onAuthenticationFailure(request, response, e);
        assertEquals("code",
            request.getSession()
                   .getAttribute(ExceptionMappingMessageCodeFailureHandler.AUTHENTICATION_MESSAGE_CODE_KEY));
        assertEquals("/target", response.getRedirectedUrl());
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void exceptionMapIsUsedIfMappingExists()
        throws Exception {
        ExceptionMappingMessageCodeFailureHandler fh = new ExceptionMappingMessageCodeFailureHandler();
        HashMap<String, String> mapping = new HashMap<String, String>();
        String nonDefaultCode = "code.ohdear";

        mapping.put("org.springframework.security.authentication.BadCredentialsException",
            nonDefaultCode);
        fh.setExceptionMappings(mapping);
        fh.setDefaultFailureUrl("/failed");
        fh.setDefaultMessageCode("code.default");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();

        fh.onAuthenticationFailure(request, response, new BadCredentialsException(""));

        assertEquals("/failed", response.getRedirectedUrl());
        assertEquals(nonDefaultCode,
            request.getSession()
                   .getAttribute(ExceptionMappingMessageCodeFailureHandler.AUTHENTICATION_MESSAGE_CODE_KEY));
    }

    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void responseIsForwardedIfUseForwardIsTrue()
        throws Exception {
        ExceptionMappingMessageCodeFailureHandler afh = new ExceptionMappingMessageCodeFailureHandler("/target",
                "code");

        afh.setUseForward(true);
        assertTrue(afh.isUseForward());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException e = mock(AuthenticationException.class);

        afh.onAuthenticationFailure(request, response, e);
        assertNull(request.getSession(false));
        assertNull(response.getRedirectedUrl());
        assertEquals("/target", response.getForwardedUrl());
        // Request scope should be used for forward
        assertEquals("code",
            request.getAttribute(
                ExceptionMappingMessageCodeFailureHandler.AUTHENTICATION_MESSAGE_CODE_KEY));
    }
}
