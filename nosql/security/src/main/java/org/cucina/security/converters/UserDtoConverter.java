package org.cucina.security.converters;

import org.springframework.core.convert.converter.Converter;

import org.cucina.security.api.UserDto;
import org.cucina.security.model.User;


/**
 *
 *
 * @author vlevine
  */
public class UserDtoConverter
    implements Converter<User, UserDto> {
    /**
     *
     *
     * @param user .
     *
     * @return .
     */
    @Override
    public UserDto convert(User user) {
        UserDto dto = new UserDto();

        dto.setEmail(user.getEmail());
        dto.setId(user.getId().longValue());
//        dto.setPassword(user.getPassword().getValue());
        dto.setUsername(user.getUsername());

        return dto;
    }
}
