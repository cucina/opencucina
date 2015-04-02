package org.cucina.security.converters;

import org.springframework.core.convert.converter.Converter;

import org.cucina.security.api.UserDto;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;


/**
 *
 *
 * @author vlevine
  */
public class DtoUserConverter
    implements Converter<UserDto, User> {
    private UserRepository userRepository;

    /**
     * Creates a new DtoUserConverter object.
     *
     * @param userRepository .
     */
    public DtoUserConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     *
     * @param dto .
     *
     * @return .
     */
    @Override
    public User convert(UserDto dto) {
        User user = userRepository.findByUsername(dto.getUsername());

        if (user == null) {
            user = new User();
        }

        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setUsername(dto.getUsername());

        return user;
    }
}
