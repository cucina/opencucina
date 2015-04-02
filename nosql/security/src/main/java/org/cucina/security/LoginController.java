package org.cucina.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.security.authentication.AuthenticationService;


/**
 *
 *
 * @author vlevine
  */
@RestController
@RequestMapping("/authenticate")
public class LoginController {
    @Autowired
    private AuthenticationService authenticationService;

    /**
     *
     *
     * @param username .
     * @param password .
     *
     * @return .
     */
    @RequestMapping(method = RequestMethod.POST)
    public Authentication login(String username, String password) {
        return authenticationService.authenticate(username, password);
    }

    /**
    *
    *
    * @return .
    */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String simpleForm() {
        return "<html><body><form method=\"POST\"  action=\"/authenticate\">" +
        "Username: <input type=\"text\" name=\"username\"><br />" +
        "Username: <input type=\"password\" name=\"password\"><br />" +
        "<input type=\"submit\" value=\"Login\">Login</form></body></html>";
    }
}
