package com.solon.airbnb.user.application.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.solon.airbnb.shared.service.GenericServiceBean;
import com.solon.airbnb.user.application.dto.SubmitCredentialsDTO;
import com.solon.airbnb.user.application.dto.UserDTO;



@Service
public class AuthServiceBean extends GenericServiceBean implements AuthService{
	
private final AuthenticationManager authenticationManager;
	
	public AuthServiceBean(AuthenticationManager authenticationManager) {
		this.authenticationManager=authenticationManager;
	}
	
	@Override
    public Authentication getAuthContext() {
		return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     *
     * @return
     */
    @Override
    public UserDTO getLoggedUser() {
        return (UserDTO) getAuthContext().getPrincipal();
    }

	@Override
	public void setAuthentication(UsernamePasswordAuthenticationToken authenticationToken) {
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);	
	}
	
	@Override
	public void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);	
	}

	@Override
	public UserDTO authenticate(SubmitCredentialsDTO submitCredentialsDTO) {
		UsernamePasswordAuthenticationToken authenticationToken
        = new UsernamePasswordAuthenticationToken(submitCredentialsDTO.getUsername(), submitCredentialsDTO.getPassword());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		setAuthentication(authentication);
		return (UserDTO) authentication.getPrincipal();
	}

}
