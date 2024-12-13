package com.solon.airbnb.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.solon.airbnb.infrastructure.security.NoAuthentication;
import com.solon.airbnb.shared.common.AirbnbConstants;
import com.solon.airbnb.shared.controller.GenericController;
import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.shared.utils.HttpUtil;
import com.solon.airbnb.user.application.dto.CreateUserDTO;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UpdateUserDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.application.utils.UserCsvExporter;
import com.solon.airbnb.user.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;


@RestController
@RequestMapping("/users")
public class UsersController extends GenericController{
	
	private static final Logger log = LoggerFactory.getLogger(UsersController.class);
	
	
	@GetMapping("/export/csv")
    public void exportUsersToCsv(@RequestBody @Valid UsersSearchRequestDTO searchObj, HttpServletResponse response) throws Exception {
		Long resultsCount = usersService.countUsers(searchObj);
		log.info("UsersController --> exportUsersToCsv --> results: {}", resultsCount);
		if (resultsCount >= AirbnbConstants.MAX_RESULTS_CSV_EXPORT) {
			throw new BusinessException("error.max.csv.results");
		}
		response.setContentType(HttpUtil.MEDIA_TYPE_CSV);
		response.setCharacterEncoding("UTF-8");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"users-results.csv\"");
		List<ReadUserDTO> results = usersService.findAllUsersForExport(searchObj);
		UserCsvExporter exporter= new UserCsvExporter(results, response);
		exporter.exportData();
    }
	
	 @PostMapping("/search")
	 public ResponseEntity<SearchResults<ReadUserDTO>> findAllUsers(@RequestBody @Valid UsersSearchRequestDTO searchObj){
		 Page<User> results = usersService.findAllUsers(searchObj);
		 List<ReadUserDTO> dtos = usersService.convertToReadUserListDTO(results.getContent());
		 return ResponseEntity.ok().body(new SearchResults<ReadUserDTO>(Math.toIntExact(results.getTotalElements()), dtos));
	 }
	 
	 @GetMapping("/{id}")
	 public ResponseEntity<ReadUserDTO> viewUser(@PathVariable(name= "id", required=true) String publicId) throws NotFoundException{
		 User user =getUserDTOByPublicId(publicId);
		 return ResponseEntity.ok(usersService.convertToReadUserDTO(user));
	 }


	 @GetMapping(value="/account")
	 public ResponseEntity<ReadUserDTO> getUserByToken(Authentication authentication) throws NotFoundException{
		 String publicId = getLoggedInUserUUID(authentication);
		 User user =getUserDTOByPublicId(publicId);
		 return ResponseEntity.ok(usersService.convertToReadUserDTO(user));
	 }


	 @NoAuthentication
	 @PostMapping
	 public ResponseEntity<ReadUserDTO> registerUser(@RequestBody @Valid CreateUserDTO user, final HttpServletRequest request) throws BusinessException {
		 log.info("UsersController->registerUser->RequestBody: {}" , user);
		 User userSaved=usersService.registerUser(user, applicationUrl(request));
		 ReadUserDTO dto = usersService.convertToReadUserDTO(userSaved);
		 return ResponseEntity.ok(dto);
	 }
	 
	 @PutMapping("/{id}")
	 public ResponseEntity<ReadUserDTO> updateUser(
			 @PathVariable(name= "id", required=true) String publicId,
			 @RequestBody @Valid UpdateUserDTO user) throws NotFoundException{
		log.info("RequestBody: {}" , user);
        User userSaved=usersService.updateUser(publicId,user);
        log.info("userSaved: {}" , userSaved);
        ReadUserDTO dto = usersService.convertToReadUserDTO(userSaved);
		return ResponseEntity.ok(dto);
	 }
	 
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name= "id",required=true) @Min(1) String publicId) throws NotFoundException{
		log.info("UsersController->deleteUser->publicId: {}" , publicId);
        usersService.deleteUser(publicId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

	@PutMapping("/{id}/activate")
	public ResponseEntity<ReadUserDTO> activateUser(
			@PathVariable(name= "id", required=true) String publicId )
			throws NotFoundException, BusinessException {
		log.info("UsersController->activateUser->publicId: {}" , publicId);
		User user =getUserDTOByPublicId(publicId);
		user = usersService.activateUser(user);
		ReadUserDTO dto = usersService.convertToReadUserDTO(user);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}/deactivate")
	public ResponseEntity<ReadUserDTO> deactivateUser(
			@PathVariable(name= "id", required=true) String publicId )
			throws NotFoundException, BusinessException {
		log.info("UsersController->activateUser->publicId: {}" , publicId);
		User user =getUserDTOByPublicId(publicId);
		user = usersService.deactivateUser(user);
		ReadUserDTO dto = usersService.convertToReadUserDTO(user);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/verifyEmail")
	public ResponseEntity<Void> verifyEmail(@RequestParam("token") String token)throws BusinessException {
		usersService.verifyEmail(token);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	protected String applicationUrl(HttpServletRequest request) {
		return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
	}

}
