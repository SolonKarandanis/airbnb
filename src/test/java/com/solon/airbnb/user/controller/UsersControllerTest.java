package com.solon.airbnb.user.controller;


import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("UsersControllerTest")
@ExtendWith(MockitoExtension.class)
public class UsersControllerTest {

    @InjectMocks
    protected UsersController controller;

    @Mock
    protected UserService usersService;

    @Mock
    protected List<User> userList;

    @Mock
    protected Page<User> results;

    @Mock
    List<ReadUserDTO> dtos;


    protected User user;
    protected UserDTO userDto;

    protected final Long userId = 1L;

    @BeforeEach
    public void setup(){
        user = TestUtil.createTestUser(userId);
        userDto = TestUtil.createTestUserDto(userId);
    }

    @DisplayName("Find All Users")
    @Test
    void testFindAllUsers(){
        UsersSearchRequestDTO searchObj = TestUtil.generateUsersSearchRequestDTO();
        when(usersService.findAllUsers(searchObj)).thenReturn(results);
        when(usersService.convertToReadUserListDTO(results.getContent())).thenReturn(dtos);

        ResponseEntity<SearchResults<ReadUserDTO>> resp = controller.findAllUsers(searchObj);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(usersService, times(1)).findAllUsers(searchObj);
        verify(usersService, times(1)).convertToReadUserListDTO(results.getContent());
    }
}
