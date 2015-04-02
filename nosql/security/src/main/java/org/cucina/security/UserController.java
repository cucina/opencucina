package org.cucina.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.cucina.security.api.UserDto;
import org.cucina.security.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 *
 * @author vlevine
  */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    /**
     *
     *
     * @return .
     */
    @RequestMapping(method = RequestMethod.GET)
    public Collection<UserDto> getAll() {
        return userService.list();
    }

    /**
     *
     *
     * @param username .
     *
     * @return .
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{username}")
    public boolean delete(@PathVariable
    String username) {
    	LOG.debug("Delete '" + username + "'");
        return userService.deleteByName(username);
    }

    /**
     *
     *
     * @param username .
     *
     * @return .
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public UserDto get(@PathVariable
    String username) {
        return userService.getByName(username);
    }

    /**
     *
     *
     * @param dto .
     *
     * @return .
     */
    @RequestMapping(method = RequestMethod.POST)
    public Long save(@RequestBody
    UserDto dto) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Saving: " + dto);
        }

        return userService.save(dto).getId();
    }
}
