package org.cucina.security.service;

import org.cucina.security.api.PermissionDto;
import org.cucina.security.api.UserDto;
import org.cucina.security.model.Permission;
import org.cucina.security.model.User;
import org.cucina.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;


/**
 * @author vlevine
 */
@Service
public class UserServiceImpl
		implements UserService {
	@Autowired
	@Qualifier(value = "myConversionService")
	private ConversionService conversionService;
	@Autowired
	private UserRepository userRepository;

	/**
	 * @param username .
	 * @return .
	 */
	@Override
	public UserDto getByName(String username) {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			return null;
		}

		UserDto dto = conversionService.convert(user, UserDto.class);

		if (user.getPermissions() != null) {
			Collection<PermissionDto> permissionDtos = new HashSet<PermissionDto>();

			for (Permission permission : user.getPermissions()) {
				permissionDtos.add(conversionService.convert(permission, PermissionDto.class));
			}

			dto.setPermissions(permissionDtos);
		}

		return dto;
	}

	/**
	 * @param username .
	 * @return .
	 */
	@Override
	public boolean deleteByName(String username) {
		return userRepository.removeByUsername(username) > 0;
	}

	/**
	 * @return .
	 */
	@Override
	public Collection<UserDto> list() {
		Collection<User> users = userRepository.findAll();
		Collection<UserDto> dtos = new HashSet<UserDto>();

		for (User user : users) {
			dtos.add(conversionService.convert(user, UserDto.class));
		}

		return dtos;
	}

	/**
	 * @param dto .
	 * @return .
	 */
	@Override
	public UserDto save(UserDto dto) {
		if (dto == null) {
			return null;
		}

		User user = conversionService.convert(dto, User.class);
		Collection<Permission> permissions = new HashSet<Permission>();

		if (dto.getPermissions() != null) {
			for (PermissionDto pdto : dto.getPermissions()) {
				permissions.add(conversionService.convert(pdto, Permission.class));
			}
		}

		user.setPermissions(permissions);
		userRepository.save(user);

		return dto;
	}
}
