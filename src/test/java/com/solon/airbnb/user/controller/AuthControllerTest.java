package com.solon.airbnb.user.controller;

import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.user.application.dto.JwtDTO;
import com.solon.airbnb.user.application.dto.SubmitCredentialsDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.service.AuthService;
import com.solon.airbnb.user.application.service.JwtService;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

@DisplayName("AuthControllerTest")
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    protected AuthController controller;

    @Mock
    protected AuthService authService;

    @Mock
    protected JwtService jwtService;

    protected User user;
    protected UserDTO userDto;

    protected final Long userId = 1L;

    @BeforeEach
    public void setup(){
        user = TestUtil.createTestUser(userId);
        userDto = TestUtil.createTestUserDto(userId);
    }

    @DisplayName("Successful Login")
    @Test
    void testLogin01() throws BusinessException {
        SubmitCredentialsDTO credentials = new SubmitCredentialsDTO();
        credentials.setUsername("admin1");
        credentials.setPassword("123");
        JwtDTO jwt = new JwtDTO("random_token",new Date());

        when(authService.authenticate(any())).thenReturn(userDto);
        when(jwtService.generateToken(userDto)).thenReturn(jwt);

        ResponseEntity<JwtDTO> resp = controller.authenticate(credentials);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), jwt);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(authService,times(1)).authenticate(any());
        verify(jwtService,times(1)).generateToken(userDto);
    }
}
