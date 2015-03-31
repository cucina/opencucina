package org.cucina.security.service;

import java.util.Collection;

import org.cucina.security.api.UserDto;


/**
 *
 *
 * @author vlevine
  */
public interface UserService {
    /**
     *
     *
     * @param username .
     *
     * @return .
     */
    UserDto getByName(String username);

    /**
     *
     *
     * @param username .
     *
     * @return .
     */
    boolean deleteByName(String username);

    /**
     *
     *
     * @return .
     */
    Collection<UserDto> list();

    /**
     *
     *
     * @param dto .
     *
     * @return .
     */
    UserDto save(UserDto dto);
}
