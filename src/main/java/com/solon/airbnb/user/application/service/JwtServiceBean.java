package com.solon.airbnb.user.application.service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.solon.airbnb.shared.exception.AirbnbException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.solon.airbnb.infrastructure.config.authorization.SecurityConstants;
import com.solon.airbnb.user.application.dto.JwtDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.domain.Authority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtServiceBean implements JwtService{

	@Value("${security.jwt.key}")
    private String signKey;

	private static final Logger log = LoggerFactory.getLogger(JwtServiceBean.class);

	@Override
	public String extractUsername(String token) throws AirbnbException{
		return extractClaim(token, Claims::getSubject);
	}

	@Override
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)throws AirbnbException{
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
//		String token = Jwts
//		        .builder()
//		        .claim("username", user.getUsername())
//		        .claim("firstName", user.getFirstName())
//		        .claim("lastName", user.getLastName())
//		        .claim("email", user.getEmail())
//		        .claim("publicId", user.getPublicId())
//		        .claim("status", user.getStatus())
//		        .claim("authorities", user.getAuthorities().stream().toList())
//		        .subject(user.getUsername())
//		        .issuedAt(new Date(System.currentTimeMillis()))
//		        .expiration(expireDate)
//				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
//		        .compact();
		String token = Jwts
				.builder()
				.claim("username", user.getUsername())
				.claim("firstName", user.getFirstName())
				.claim("lastName", user.getLastName())
				.claim("email", user.getEmail())
				.claim("publicId", user.getPublicId())
				.claim("status", user.getStatus())
				.claim("authorities", user.getAuthorities().stream().toList())
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512,signKey.getBytes())
				.compact();
		return new JwtDTO(token, expireDate);
	}
	
	@Override
	public Claims extractAllClaims(String token) throws AirbnbException {
//		return Jwts.parser()
//				.verifyWith(getSigningKey())
//				.build()
//				.parseSignedClaims(token)
//				.getPayload();
		try{
			return Jwts
					.parser()
					.setSigningKey(signKey.getBytes())
					.parseClaimsJws(token.replace(SecurityConstants.BEARER_TOKEN_PREFIX, ""))
					.getBody();

		}catch (MalformedJwtException e) {
			log.error("Invalid  token: {}", e.getMessage());
			throw new AirbnbException("error.invalid.token");
		}

	}

	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) throws AirbnbException{
		final String username = extractUsername(token);
	    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token)throws AirbnbException {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) throws AirbnbException{
	    return extractClaim(token, Claims::getExpiration);
	}
	
	private static List<String> getAuthorities(Collection<Authority> authorities) {
        return authorities.stream().map(Authority::getName).filter(Objects::nonNull).toList();
    }
	
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(signKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
