package com.solon.airbnb.user.application.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.solon.airbnb.infrastructure.config.authorization.SecurityConstants;
import com.solon.airbnb.user.application.dto.JwtDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.domain.Authority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtServiceBean implements JwtService{

	@Value("${security.jwt.key}")
    private String signKey;

	@Override
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
	    return claimsResolver.apply(claims);
	}

	@Override
	public JwtDTO generateToken(UserDTO user) {
		return generateToken(new HashMap<>(), user);
	}
	
	@Override
	public JwtDTO generateToken(Map<String, Object> extraClaims, UserDTO user) {
		Date expireDate = new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME);
		String token = Jwts
		        .builder()
		        .claim("username", user.getUsername())
		        .claim("firstName", user.getFirstName())
		        .claim("lastName", user.getLastName())
		        .claim("email", user.getEmail())
		        .claim("publicId", user.getPublicId())
		        .claim("status", user.getStatus())
		        .claim("authorities", user.getAuthorityNames())
		        .subject(user.getUsername())
		        .issuedAt(new Date(System.currentTimeMillis()))
		        .expiration(expireDate)	        
		        .signWith(getSecretKey())
		        .compact();
		return new JwtDTO(token, expireDate);
	}
	
	@Override
	public Claims extractAllClaims(String token) {
	 	return Jwts.parser()
	 			.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(token)
		        .getPayload();
	}

	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
	    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
	    return extractClaim(token, Claims::getExpiration);
	}
	
	private static List<String> getAuthorities(Collection<Authority> authorities) {
        return authorities.stream().map(Authority::getName).filter(Objects::nonNull).toList();
    }
	
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(signKey.getBytes());
	}
}
