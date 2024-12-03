package com.solon.airbnb.user.controller;


import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.*;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.util.TestConstants;
import com.solon.airbnb.util.TestUtil;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Mock
    protected HttpServletRequest request;

    @Mock
    protected Authentication authentication;


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
        when(usersService.getByPublicId(TestConstants.TEST_USER_PUBLIC_ID)).thenReturn(Optional.of(user));
        when(usersService.convertToReadUserDTO(user)).thenReturn(readUserDTO);

        ResponseEntity<ReadUserDTO> resp = controller.viewUser(TestConstants.TEST_USER_PUBLIC_ID);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), readUserDTO);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(usersService, times(1)).getByPublicId(TestConstants.TEST_USER_PUBLIC_ID);
        verify(usersService, times(1)).convertToReadUserDTO(user);
    }

    @DisplayName("View User (not found)")
    @Test
    void testViewUser02(){
        when(usersService.getByPublicId(TestConstants.TEST_INVALID_USER_PUBLIC_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->{
            controller.viewUser(TestConstants.TEST_INVALID_USER_PUBLIC_ID);
        });
        assertEquals("error.user.not.found",exception.getLocalizedMessage());

        verify(usersService,times(1)).getByPublicId(TestConstants.TEST_INVALID_USER_PUBLIC_ID);
    }

    @DisplayName("Get user by token")
    @Test
    void testGetUserByToken(){
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(usersService.getByPublicId(TestConstants.TEST_USER_PUBLIC_ID)).thenReturn(Optional.of(user));
        when(usersService.convertToReadUserDTO(user)).thenReturn(readUserDTO);

        ResponseEntity<ReadUserDTO> resp = controller.getUserByToken(authentication);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), readUserDTO);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(usersService, times(1)).getByPublicId(TestConstants.TEST_USER_PUBLIC_ID);
        verify(usersService, times(1)).convertToReadUserDTO(user);
    }

    @DisplayName("Register user")
    @Test
    void testRegisterUser() throws BusinessException {
        CreateUserDTO dto = new CreateUserDTO();
        String url = "http://null:0null";
        when(usersService.registerUser(dto,url)).thenReturn(user);
        when(usersService.convertToReadUserDTO(user)).thenReturn(readUserDTO);

        ResponseEntity<ReadUserDTO> resp = controller.registerUser(dto,request);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), readUserDTO);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(usersService, times(1)).registerUser(dto,url);
        verify(usersService, times(1)).convertToReadUserDTO(user);
    }

    @DisplayName("Register user")
    @Test
    void testUpdateUser(){
        UpdateUserDTO dto = new UpdateUserDTO();
        when(usersService.updateUser(TestConstants.TEST_USER_PUBLIC_ID,dto)).thenReturn(user);
        when(usersService.convertToReadUserDTO(user)).thenReturn(readUserDTO);

        ResponseEntity<ReadUserDTO> resp = controller.updateUser(TestConstants.TEST_USER_PUBLIC_ID,dto);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), readUserDTO);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(usersService, times(1)).updateUser(TestConstants.TEST_USER_PUBLIC_ID,dto);
        verify(usersService, times(1)).convertToReadUserDTO(user);
    }
}
