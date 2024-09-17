package com.solon.airbnb.user.application.service;

import com.solon.airbnb.user.application.dto.ReadUserDTO;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    public Optional<ReadUserDTO> getByEmail(String email);

    public Optional<ReadUserDTO> getByPublicId(UUID publicId);
}
