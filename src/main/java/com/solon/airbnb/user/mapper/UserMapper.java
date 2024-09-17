package com.solon.airbnb.user.mapper;

import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.domain.Authority;
import com.solon.airbnb.user.domain.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO readUserDTOToUser(User user);

    default String mapAuthoritiesToString(Authority authority) {
        return authority.getName();
    }

}