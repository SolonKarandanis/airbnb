package com.solon.airbnb.user.application.service;

import java.util.Optional;
import java.util.UUID;

import com.solon.airbnb.user.application.dto.ReadUserDTO;

public interface UserService{

    public Optional<ReadUserDTO> getByEmail(String email);

    public Optional<ReadUserDTO> getByPublicId(UUID publicId);
}
