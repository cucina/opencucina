
package org.cucina.security.authentication.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Simple implementation of <code>org.springframework.security.web.authentication.logout.LogoutSuccessHandler</code>
 * which returns json representation of <code>LoginStatus</code> which holds the username.

 *
 */
public class LogoutSuccessHandler
    implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    /**
     * JAVADOC Method Level Comments
     *
     * @param request JAVADOC.
     * @param response JAVADOC.
     * @param authentication JAVADOC.
     *
     * @throws IOException JAVADOC.
     * @throws ServletException JAVADOC.
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication)
        throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        LoginStatus status = new LoginStatus(true, false, null, null);
        OutputStream out = response.getOutputStream();

        mapper.writeValue(out, status);
    }
}
