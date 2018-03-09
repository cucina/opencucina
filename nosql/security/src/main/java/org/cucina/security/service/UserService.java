package org.cucina.security.service;

import org.cucina.security.api.UserDto;

import java.util.Collection;


/**
 * @author vlevine
 */
public interface UserService {
	/**
	 * @param username .
	 * @return .
	 */
	UserDto getByName(String username);

	/**
	 * @param username .
	 * @return .
	 */
	boolean deleteByName(String username);

	/**
	 * @return .
	 */
	Collection<UserDto> list();

	/**
	 * @param dto .
	 * @return .
	 */
	UserDto save(UserDto dto);
}
