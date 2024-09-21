package com.solon.airbnb.user.application.service;

import java.util.List;

import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.domain.User;


public interface BaseUserAccountService {
	
	public UserDTO convertToDTO(User user, boolean addAuthorities);
	public User convertToEntity(UserDTO dto);
	public List<UserDTO> convertToDTOList(List<User> userList, boolean addAuthorities);
	public ReadUserDTO convertToReadUserDTO(User user);
	public List<ReadUserDTO> convertToReadUserListDTO(List<User> userList);

}
