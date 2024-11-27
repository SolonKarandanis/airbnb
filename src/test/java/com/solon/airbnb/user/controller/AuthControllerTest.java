package com.solon.airbnb.user.controller;

import com.solon.airbnb.user.application.service.AuthService;
import com.solon.airbnb.user.application.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("AuthControllerTest")
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    protected AuthController controller;

    @Mock
    protected AuthService authService;

    @Mock
    protected JwtService jwtService;
}
