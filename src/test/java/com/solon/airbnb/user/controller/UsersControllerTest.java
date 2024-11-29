package com.solon.airbnb.user.controller;


import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UsersControllerTest")
@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {

    @InjectMocks
    protected UsersController controller;

    @Mock
    protected UserService usersService;


    protected User user;
    protected UserDTO userDto;
}
