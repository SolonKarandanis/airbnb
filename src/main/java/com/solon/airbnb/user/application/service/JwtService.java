package com.solon.airbnb.user.application.service;

import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import com.solon.airbnb.user.application.dto.JwtDTO;
import com.solon.airbnb.user.application.dto.UserDTO;

import io.jsonwebtoken.Claims;

public interface JwtService {
	
public String extractUsername(String token);
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
	
	public Claims extractAllClaims(String token);
	
	public JwtDTO generateToken(UserDTO user);
	
	public JwtDTO generateToken(Map<String, Object> extraClaims,UserDTO user);
	
	public boolean isTokenValid(String token, UserDetails userDetails);

}
