package com.solon.airbnb.user.application.service;

import java.util.List;
import java.util.Optional;

import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.user.application.dto.UpdateUserDTO;
import org.springframework.data.domain.Page;

import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.CreateUserDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.domain.User;


public interface UserService extends BaseUserAccountService{

	public User findById(Long id) throws NotFoundException;
	public List<User> findAllUsers();
    public Optional<ReadUserDTO> getByEmail(String email)throws NotFoundException;
    public Optional<User> getByPublicId(String publicId)throws NotFoundException;
    public void deleteUser(String uuid) throws NotFoundException;
    public Page<User> findAllUsers(UsersSearchRequestDTO searchObj);
    public User registerUser(CreateUserDTO dto, String applicationUrl) throws BusinessException;
    public User updateUser(UpdateUserDTO dto) throws NotFoundException;
    public void verifyEmail(String token) throws BusinessException;
    public User activateUser(User user) throws BusinessException;
    public User deactivateUser(User user) throws BusinessException;

}
