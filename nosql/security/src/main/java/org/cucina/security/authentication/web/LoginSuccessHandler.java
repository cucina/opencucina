
package org.cucina.security.authentication.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Handles successful login, returning JSON object to that effect.

 *
 */
public class LoginSuccessHandler
    implements AuthenticationSuccessHandler {
    /**
     * JAVADOC Method Level Comments
     *
     * @param request JAVADOC.
     * @param response JAVADOC.
     * @param auth JAVADOC.
     *
     * @throws IOException JAVADOC.
     * @throws ServletException JAVADOC.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication auth)
        throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        LoginStatus status = new LoginStatus(true, auth.isAuthenticated(), auth.getName(), null);
        OutputStream out = response.getOutputStream();

        mapper.writeValue(out, status);
    }
}
