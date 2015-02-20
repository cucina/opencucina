
package org.cucina.security.authentication.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Test LogoutSuccessHandler.
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LogoutSuccessHandlerTest {
    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testLogoutSuccess()
        throws Exception {
        LogoutSuccessHandler logoutHandler = new LogoutSuccessHandler();

        HttpServletResponse response = mock(HttpServletResponse.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream os = new DelegatingServletOutputStream(baos);

        when(response.getOutputStream()).thenReturn(os);

        Authentication token = new TestingAuthenticationToken("user", null);

        token.setAuthenticated(false);
        logoutHandler.onLogoutSuccess(null, response, token);
        assertEquals("{\"errorMessage\":null,\"username\":null,\"loggedIn\":false,\"success\":true}",
            baos.toString());
        verify(response).getOutputStream();
    }
}
