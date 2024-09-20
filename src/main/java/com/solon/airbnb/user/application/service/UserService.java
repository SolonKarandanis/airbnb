package com.solon.airbnb.user.application.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserInputDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.domain.User;


public interface UserService{

    public Optional<ReadUserDTO> getByEmail(String email);
    public Optional<ReadUserDTO> getByPublicId(UUID publicId);
    public void deleteUser(String uuid) throws NotFoundException;
    public Page<User> findAllUsers(UsersSearchRequestDTO searchObj,Pageable page);
    public User createUser(UserInputDTO dto) throws NotFoundException;
    public User updateUser(UserInputDTO dto) throws NotFoundException;
}
