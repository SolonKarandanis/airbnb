package com.solon.airbnb.user.application.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.solon.airbnb.user.application.dto.serializer.CustomAuthorityDeserializer;
import com.solon.airbnb.user.domain.AccountStatus;
import com.solon.airbnb.user.domain.Authority;


public class UserDTO implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
    private String password;
	private String lastName;
	private String firstName;
	private String email;
	private String imageUrl;
	private UUID publicId;
	private AccountStatus status;
	private List<Authority> authorityEntities= new ArrayList<>();
    private List<String> authorityNames = new ArrayList<>();
	private Boolean isVerified;
    
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
    	 return authorityEntities.stream()
         		.map(role-> new SimpleGrantedAuthority(role.getName()))
         		.toList();
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public UUID getPublicId() {
		return publicId;
	}
	
	public void setPublicId(UUID publicId) {
		this.publicId = publicId;
	}
	
	public AccountStatus getStatus() {
		return status;
	}
	
	public void setStatus(AccountStatus status) {
		this.status = status;
	}


	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getAuthorityNames() {
		return authorityNames;
	}
	public void setAuthorityNames(List<String> authorityNames) {
		this.authorityNames = authorityNames;
	}

	public List<Authority> getAuthorityEntities() {
		return authorityEntities;
	}

	public void setAuthorityEntities(List<Authority> authorityEntities) {
		this.authorityEntities = authorityEntities;
	}

	public Boolean getVerified() {
		return isVerified;
	}

	public void setVerified(Boolean verified) {
		isVerified = verified;
	}
}
