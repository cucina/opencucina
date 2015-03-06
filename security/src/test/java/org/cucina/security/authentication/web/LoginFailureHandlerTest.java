package org.cucina.security.authentication.web;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.DelegatingServletOutputStream;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Test <code>LoginFailureHandler</code>
 *
 * @author $Author: $
 * @version $Revision: $
  */
public class LoginFailureHandlerTest {
    /**
     * JAVADOC Method Level Comments
     *
     * @throws Exception JAVADOC.
     */
    @Test
    public void testOnAuthenticationFailure()
        throws Exception {
        LoginFailureHandler failureHandler = new LoginFailureHandler();

        HttpServletResponse response = mock(HttpServletResponse.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream os = new DelegatingServletOutputStream(baos);

        when(response.getOutputStream()).thenReturn(os);
        failureHandler.onAuthenticationFailure(null, response, null);
        System.err.println(baos);
        assertEquals("{\"errorMessage\":\"" + LoginFailureHandler.SYSTEM_ERROR_MSG +
            "\",\"username\":null,\"loggedIn\":false,\"success\":false}", baos.toString());
        verify(response).getOutputStream();
    }
}
