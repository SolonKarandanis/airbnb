package com.solon.airbnb.user.application.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.solon.airbnb.user.application.dto.SubmitCredentialsDTO;
import com.solon.airbnb.user.application.dto.UserDTO;


public interface AuthService {

	public Authentication getAuthContext();
    public UserDTO getLoggedUser();
    public void setAuthentication(UsernamePasswordAuthenticationToken authenticationToken);
    public void setAuthentication(Authentication authentication);
    public UserDTO authenticate(SubmitCredentialsDTO submitCredentialsDTO);
}
