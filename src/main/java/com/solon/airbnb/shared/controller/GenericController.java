package com.solon.airbnb.shared.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class GenericController {
	
	protected static final String USER_NOT_FOUND="User not found";
	
	@Autowired
	protected UserService usersService;
	
	protected User getLoggedInUser(Authentication authentication) throws NotFoundException, AirbnbException {
    	String uuid = getLoggedInUserUUID(authentication);
    	return usersService.getByPublicId(uuid).get();
    }
	 
	protected String getLoggedInUserUUID(Authentication authentication) {
		UserDTO dto = (UserDTO)authentication.getPrincipal();
    	return dto.getPublicId().toString();
	}
	
	protected Long getLoggedInUserId(Authentication authentication) {
		UserDTO dto = (UserDTO)authentication.getPrincipal();
    	return dto.getId();
	}
	    
    protected String getLoggedInUserName(Authentication authentication) {
    	UserDTO dto = (UserDTO)authentication.getPrincipal();
    	return dto.getUsername();
    }
    
    protected User getUserDTOByPublicId(String publicId) {
    	return usersService.getByPublicId(publicId)
    			.orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

	protected String getCookieValue(HttpServletRequest req, String cookieName) {
	    return Arrays.stream(req.getCookies())
	            .filter(c -> c.getName().equals(cookieName))
	            .findFirst()
	            .map(Cookie::getValue)
	            .orElse(null);
	}
	
	protected String getHeaderValue(HttpServletRequest req, String headerName) {
	    return req.getHeader(headerName);
	}
	
	protected static String getBody(HttpServletRequest request) throws IOException {
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(
	            		new InputStreamReader(inputStream,Charset.defaultCharset()));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    String body = stringBuilder.toString();
	    return body;
	}
	
	protected static <T> T getBody(HttpServletRequest request,Class<T> objectClass) 
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(request.getInputStream(),objectClass);
	}
		

}
