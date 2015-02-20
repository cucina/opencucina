
package org.cucina.security.authentication.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Handles login failure returning JSON object to that effect.

 *
 */
public class LoginFailureHandler
    implements AuthenticationFailureHandler {
    /** This is a field JAVADOC */
    public static final String SYSTEM_ERROR_MSG = "Login failed. Please contact system administrator.";

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
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException auth)
        throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        String msg = ((auth == null) || StringUtils.isEmpty(auth.getLocalizedMessage()))
            ? SYSTEM_ERROR_MSG : auth.getLocalizedMessage();

        LoginStatus status = new LoginStatus(false, false, null, msg);
        OutputStream out = response.getOutputStream();

        mapper.writeValue(out, status);
    }
}
