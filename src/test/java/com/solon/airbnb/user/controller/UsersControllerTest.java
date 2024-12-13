package com.solon.airbnb.user.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.CreateUserDTO;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UpdateUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.util.TestConstants;
import com.solon.airbnb.util.TestUtil;

import jakarta.servlet.http.HttpServletRequest;

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
    protected HttpServletRequest request;

    @Mock
    protected Authentication authentication;


    protected User user;
    protected UserDTO userDto;
    protected ReadUserDTO readUserDTO;
    protected List<ReadUserDTO> dtos;
    protected MockHttpServletResponse response;

    protected final Long userId = 1L;

    @BeforeEach
    public void setup(){
        user = TestUtil.createTestUser(userId);
        userDto = TestUtil.createTestUserDto(userId);
        readUserDTO = TestUtil.createTestReadUserDTO(TestConstants.TEST_USER_PUBLIC_ID);
        response = new MockHttpServletResponse();
        dtos = new ArrayList<>();
        dtos.add(readUserDTO);
    }

    @DisplayName("Export to csv ( max results)")
    @Test
    void testExportUsersToCsv() throws AirbnbException {
        UsersSearchRequestDTO searchObj = TestUtil.generateUsersSearchRequestDTO();
        when(usersService.countUsers(searchObj)).thenReturn(11000L);

        BusinessException exception = assertThrows(BusinessException.class,()->{
            controller.exportUsersToCsv(searchObj,response);
        });
        assertEquals("error.max.csv.results",exception.getLocalizedMessage());

        verify(usersService,times(1)).countUsers(searchObj);
    }

    @DisplayName("Export to csv")
    @Test
    void testExportUsersToCsv02() throws Exception {
    	response.setOutputStreamAccessAllowed(true);
        UsersSearchRequestDTO searchObj = TestUtil.generateUsersSearchRequestDTO();
        
        when(usersService.countUsers(searchObj)).thenReturn(500L);
        when(usersService.findAllUsersForExport(searchObj)).thenReturn(dtos);

        controller.exportUsersToCsv(searchObj,response);

        verify(usersService,times(1)).countUsers(searchObj);
        verify(usersService,times(1)).findAllUsersForExport(searchObj);
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
        assertEquals(resp.getBody().getCountRows(),Math.toIntExact(results.getTotalElements()));
        assertEquals(resp.getBody().getList(),dtos);
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

    @DisplayName("Update user")
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

    @DisplayName("Delete user")
    @Test
    void testDeleteUser(){
        doNothing().when(usersService).deleteUser(TestConstants.TEST_USER_PUBLIC_ID);

        ResponseEntity<Void> resp = controller.deleteUser(TestConstants.TEST_USER_PUBLIC_ID);
        assertNotNull(resp);
        assertNull(resp.getBody());
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT));

        verify(usersService, times(1)).deleteUser(TestConstants.TEST_USER_PUBLIC_ID);
    }

    @DisplayName("Activate User")
    @Test
    void testActivateUser() throws BusinessException {
        when(usersService.getByPublicId(TestConstants.TEST_USER_PUBLIC_ID)).thenReturn(Optional.of(user));
        when(usersService.activateUser(user)).thenReturn(user);
        when(usersService.convertToReadUserDTO(user)).thenReturn(readUserDTO);

        ResponseEntity<ReadUserDTO> resp = controller.activateUser(TestConstants.TEST_USER_PUBLIC_ID);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), readUserDTO);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(usersService, times(1)).getByPublicId(TestConstants.TEST_USER_PUBLIC_ID);
        verify(usersService, times(1)).activateUser(user);
        verify(usersService, times(1)).convertToReadUserDTO(user);
    }

    @DisplayName("Activate User (not found)")
    @Test
    void testActivateUser02(){
        when(usersService.getByPublicId(TestConstants.TEST_INVALID_USER_PUBLIC_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->{
            controller.activateUser(TestConstants.TEST_INVALID_USER_PUBLIC_ID);
        });
        assertEquals("error.user.not.found",exception.getLocalizedMessage());

        verify(usersService,times(1)).getByPublicId(TestConstants.TEST_INVALID_USER_PUBLIC_ID);
    }

    @DisplayName("Deactivate User")
    @Test
    void testDeactivateUser() throws BusinessException {
        when(usersService.getByPublicId(TestConstants.TEST_USER_PUBLIC_ID)).thenReturn(Optional.of(user));
        when(usersService.deactivateUser(user)).thenReturn(user);
        when(usersService.convertToReadUserDTO(user)).thenReturn(readUserDTO);

        ResponseEntity<ReadUserDTO> resp = controller.deactivateUser(TestConstants.TEST_USER_PUBLIC_ID);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), readUserDTO);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(usersService, times(1)).getByPublicId(TestConstants.TEST_USER_PUBLIC_ID);
        verify(usersService, times(1)).deactivateUser(user);
        verify(usersService, times(1)).convertToReadUserDTO(user);
    }

    @DisplayName("Deactivate User (not found)")
    @Test
    void testDeactivateUser02(){
        when(usersService.getByPublicId(TestConstants.TEST_INVALID_USER_PUBLIC_ID)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->{
            controller.deactivateUser(TestConstants.TEST_INVALID_USER_PUBLIC_ID);
        });
        assertEquals("error.user.not.found",exception.getLocalizedMessage());

        verify(usersService,times(1)).getByPublicId(TestConstants.TEST_INVALID_USER_PUBLIC_ID);
    }

    @DisplayName("Verify Email")
    @Test
    void testVerifyEmail() throws BusinessException {
        String token = "test_token";
        doNothing().when(usersService).verifyEmail(token);

        ResponseEntity<Void> resp = controller.verifyEmail(token);
        assertNotNull(resp);
        assertNull(resp.getBody());
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.NO_CONTENT));

        verify(usersService,times(1)).verifyEmail(token);
    }
}
