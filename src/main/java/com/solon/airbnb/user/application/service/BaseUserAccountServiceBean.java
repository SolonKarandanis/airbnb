package com.solon.airbnb.user.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.solon.airbnb.shared.service.GenericServiceBean;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.mapper.UserMapper;

@Service(value="BaseUserAccountService")
public class BaseUserAccountServiceBean extends GenericServiceBean implements BaseUserAccountService{

	protected static final String USER_NOT_FOUND="User not found";
	
	@Autowired
	protected UserMapper userMapper;

	@Override
	public UserDTO convertToDTO(User user, boolean addAuthorities) {
		UserDTO dto = new UserDTO();
		dto.setUsername(user.getUsername());
		dto.setPassword(user.getPassword());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setPublicId(user.getPublicId());
		dto.setStatus(user.getStatus());
		if(addAuthorities) {
			dto.setAuthorityEntities(user.getAuthorities().stream().toList());
		}
		return dto;
	}

	@Override
	public User convertToEntity(UserDTO dto) {
		User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPublicId(dto.getPublicId());
        user.setStatus(dto.getStatus());
        return user;
	}

	@Override
	public List<UserDTO> convertToDTOList(List<User> userList, boolean addAuthorities) {
		return userList.stream()
                .map(user-> convertToDTO(user,addAuthorities))
                .toList();
	}

	@Override
	public List<ReadUserDTO> convertToReadUserListDTO(List<User> userList) {
		return userList.stream()
				.map(userMapper::readUserDTOToUser)
				.toList();
	}

	@Override
	public ReadUserDTO convertToReadUserDTO(User user) {
		return userMapper.readUserDTOToUser(user);
	}

}
