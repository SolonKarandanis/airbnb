package com.solon.airbnb.user.application.service;

import com.solon.airbnb.user.application.dto.ReadUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserServiceBean implements UserService{
    @Override
    public Optional<ReadUserDTO> getByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<ReadUserDTO> getByPublicId(UUID publicId) {
        return Optional.empty();
    }
}
