package com.solon.airbnb.user.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solon.airbnb.shared.controller.GenericController;
import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserInputDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.application.utils.UserExcelExportUtils;
import com.solon.airbnb.user.domain.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/users")
public class UsersController extends GenericController{
	
	private static final Logger log = LoggerFactory.getLogger(UsersController.class);
	
	
	@GetMapping("/export-to-excel")
    public void exportToExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Customers_Information.xlsx";
        response.setHeader(headerKey, headerValue);
        List<User> allUsers = usersService.findAllUsers();
        UserExcelExportUtils exportUtils = new UserExcelExportUtils(allUsers);
    }
	
	 @PostMapping("/search")
	 public ResponseEntity<SearchResults<ReadUserDTO>> findAllUsers(@RequestBody @Valid UsersSearchRequestDTO searchObj){
		 Page<User> results = usersService.findAllUsers(searchObj);
		 List<ReadUserDTO> dtos = usersService.convertToReadUserListDTO(results.getContent());
		 return ResponseEntity.ok().body(new SearchResults<ReadUserDTO>(Math.toIntExact(results.getTotalElements()), dtos));
	 }
	 
	 @GetMapping("/{id}")
	 public ResponseEntity<ReadUserDTO> viewUser(@PathVariable(name= "id", required=true) String publicId) throws NotFoundException{
		 return ResponseEntity.ok(getUserDTOByPublicId(publicId));
	 }
	 
	 @GetMapping(value="/account")
	 public ResponseEntity<ReadUserDTO> getUserByToken(Authentication authentication) throws NotFoundException{
		 String publicId = getLoggedInUserUUID(authentication);
		 return ResponseEntity.ok(getUserDTOByPublicId(publicId));
	 }
	 
	 @PostMapping
	 public ResponseEntity<ReadUserDTO> saveUser(@RequestBody @Valid UserInputDTO user) throws NotFoundException{
		 log.info("UsersController->saveUser->RequestBody: {}" , user);
		 User userSaved=usersService.createUser(user);
		 ReadUserDTO dto = usersService.convertToReadUserDTO(userSaved);
		 return ResponseEntity.ok(dto);
	 }
	 
	 @PutMapping
	 public ResponseEntity<ReadUserDTO> updateUser(@RequestBody @Valid UserInputDTO user) throws NotFoundException{
		log.info("RequestBody: {}" , user);
        User userSaved=usersService.updateUser(user);
        log.info("userSaved: {}" , userSaved);
        ReadUserDTO dto = usersService.convertToReadUserDTO(userSaved);
		return ResponseEntity.ok(dto);
	 }
	 
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name= "id",required=true) @Min(1) String publicId) throws NotFoundException{
        usersService.deleteUser(publicId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
