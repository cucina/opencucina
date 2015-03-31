
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
 * Test <code>LoginSuccessHandler</code>
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LoginSuccessHandlerTest {
    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testOnAuthenticationSuccess()
        throws Exception {
        LoginSuccessHandler failureHandler = new LoginSuccessHandler();

        HttpServletResponse response = mock(HttpServletResponse.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream os = new DelegatingServletOutputStream(baos);

        when(response.getOutputStream()).thenReturn(os);

        Authentication token = new TestingAuthenticationToken("user", null);

        token.setAuthenticated(true);
        failureHandler.onAuthenticationSuccess(null, response, token);
        System.err.println(baos);
        assertEquals("{\"errorMessage\":null,\"username\":\"user\",\"loggedIn\":true,\"success\":true}",
            baos.toString());
        verify(response).getOutputStream();
    }
}
