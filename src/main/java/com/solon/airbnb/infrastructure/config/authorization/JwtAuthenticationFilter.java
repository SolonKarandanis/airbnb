package com.solon.airbnb.infrastructure.config.authorization;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.service.AuthService;
import com.solon.airbnb.user.application.service.JwtService;
import com.solon.airbnb.user.domain.AccountStatus;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthService authService;
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		final String authHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
	    final String jwt;
	    final String username; 
	    if (!isAuthorizationHeader(authHeader)) {
	        filterChain.doFilter(request, response);
	        return;
	    }
	    jwt = authHeader.substring(SecurityConstants.BEARER_TOKEN_PREFIX.length());
	    username = jwtService.extractUsername(jwt);
	    
	    if (username != null && authService.getAuthContext() == null) {
	    	UsernamePasswordAuthenticationToken authToken = getAuthentication(request,jwt);
	    	authService.setAuthentication(authToken);
	    	filterChain.doFilter(request, response);
	    }
		
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,String jwt) {
		 String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
		 if (token != null) {
			 Claims claims = jwtService.extractAllClaims(token);
			 UserDTO user = new UserDTO();
			 user.setUsername(claims.get("sub", String.class));
			 user.setPassword(claims.get("password", String.class));
			 user.setFirstName(claims.get("firstName", String.class));
			 user.setLastName(claims.get("lastName", String.class));
			 user.setEmail(claims.get("email", String.class));
			 user.setPublicId(UUID.fromString(claims.get("publicId", String.class)));
			 user.setStatus(AccountStatus.valueOf(claims.get("status", String.class)));
			 if (user.getUsername() != null && jwtService.isTokenValid(jwt, user)) {
				 List<String> authorities = claims.get("authorities", List.class);
				 List<SimpleGrantedAuthority> simpleGrantedAuthorities= authorities.stream()
						 .map(SimpleGrantedAuthority::new)
						 .toList();
				 return new UsernamePasswordAuthenticationToken(user, null, simpleGrantedAuthorities);
			 }
		 }
		 return null;
	 }
	
	private boolean isAuthorizationHeader(String authHeader) {
        if (authHeader == null || authHeader.trim().isEmpty() || !authHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX) ) {
            return false;
        } 
        return true;
	 }
}