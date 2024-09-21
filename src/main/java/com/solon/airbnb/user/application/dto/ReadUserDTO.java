package com.solon.airbnb.user.application.dto;

import java.util.Set;
import java.util.UUID;

import com.solon.airbnb.user.domain.AccountStatus;

public record ReadUserDTO(UUID publicId,
                          String firstName,
                          String lastName,
                          String email,
                          String imageUrl,
                          AccountStatus status,
                          Set<String> authorities) {
}
