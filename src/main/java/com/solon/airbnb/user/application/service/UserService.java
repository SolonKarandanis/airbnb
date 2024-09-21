package com.solon.airbnb.user.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserInputDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.domain.User;


public interface UserService extends BaseUserAccountService{

	public User findById(Long id) throws NotFoundException;
	public List<User> findAllUsers();
    public Optional<ReadUserDTO> getByEmail(String email)throws NotFoundException;
    public Optional<ReadUserDTO> getByPublicId(String publicId)throws NotFoundException;
    public void deleteUser(String uuid) throws NotFoundException;
    public Page<User> findAllUsers(UsersSearchRequestDTO searchObj);
    public User createUser(UserInputDTO dto) throws NotFoundException;
    public User updateUser(UserInputDTO dto) throws NotFoundException;
}
