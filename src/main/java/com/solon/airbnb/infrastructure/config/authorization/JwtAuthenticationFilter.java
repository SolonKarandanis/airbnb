package com.solon.airbnb.infrastructure.config.authorization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.user.application.dto.AuthorityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.service.JwtService;
import com.solon.airbnb.user.domain.AccountStatus;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtService jwtService;

	
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
        try {
            username = jwtService.extractUsername(jwt);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UsernamePasswordAuthenticationToken authToken = getAuthentication(request,jwt);
				SecurityContextHolder.getContext().setAuthentication(authToken);
				filterChain.doFilter(request, response);
			}
        } catch (AirbnbException e) {
            throw new RuntimeException(e);
        }
	}
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request,String jwt) throws AirbnbException {
		 String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
		 if (token != null) {
             Claims claims = null;
             try {
                 claims = jwtService.extractAllClaims(token);
             } catch (AirbnbException e) {
                 throw new RuntimeException(e);
             }
             UserDTO user = new UserDTO();
			 user.setUsername(claims.get("sub", String.class));
			 user.setPassword(claims.get("password", String.class));
			 user.setFirstName(claims.get("firstName", String.class));
			 user.setLastName(claims.get("lastName", String.class));
			 user.setEmail(claims.get("email", String.class));
			 user.setPublicId(UUID.fromString(claims.get("publicId", String.class)));
			 user.setStatus(AccountStatus.valueOf(claims.get("status", String.class)));
			 if (user.getUsername() != null && jwtService.isTokenValid(jwt, user)) {
				 List<String> authorityClaims= (List<String>)claims.get("authorities");
				 log.info("JwtAuthenticationFilter -> claims -> authorities : {}",claims.get("authorities"));
				 List<AuthorityDTO> authorities = new ArrayList<>();
				 for(String authClaim : authorityClaims){
					 authorities.add(new AuthorityDTO(authClaim));
				 }
				 List<SimpleGrantedAuthority> simpleGrantedAuthorities= authorities.stream()
						 .map(authority-> new SimpleGrantedAuthority(authority.getAuthority()))
						 .toList();
				 return new UsernamePasswordAuthenticationToken(user, null, simpleGrantedAuthorities);
			 }
		 }
		 return null;
	 }
//List<String> authorities = mapper.convertValue(claims.get("authorities", List.class), new TypeReference<List<String>>() { })
	
	private boolean isAuthorizationHeader(String authHeader) {
        if (authHeader == null || authHeader.trim().isEmpty() || !authHeader.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX) ) {
            return false;
        } 
        return true;
	 }
}
