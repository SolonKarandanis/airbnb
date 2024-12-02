package com.solon.airbnb.user.controller;


import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.util.TestConstants;
import com.solon.airbnb.util.TestUtil;
import jakarta.servlet.http.HttpServletResponse;
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

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    protected HttpServletResponse response;


    protected User user;
    protected UserDTO userDto;
    protected ReadUserDTO readUserDTO;

    protected final Long userId = 1L;

    @BeforeEach
    public void setup(){
        user = TestUtil.createTestUser(userId);
        userDto = TestUtil.createTestUserDto(userId);
        readUserDTO = TestUtil.createTestReadUserDTO(TestConstants.TEST_USER_PUBLIC_ID);
    }

//    @DisplayName("Export to Excel")
//    @Test
//    void testExportToExcel(){
//        when(usersService.findAllUsers()).thenReturn(userList);
//    }

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

    @DisplayName("View User")
    @Test
    void testViewUser(){
        when(usersService.convertToReadUserDTO(user)).thenReturn(readUserDTO);

        ResponseEntity<ReadUserDTO> resp = controller.viewUser("1");
        assertNotNull(resp);
        assertNotNull(resp.getBody());
//        assertThat();
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(usersService, times(1)).convertToReadUserDTO(user);
    }
}
