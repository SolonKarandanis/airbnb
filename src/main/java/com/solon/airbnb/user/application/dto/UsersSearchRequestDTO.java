package com.solon.airbnb.user.application.dto;

import com.solon.airbnb.shared.dto.SearchRequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersSearchRequestDTO extends SearchRequestDTO{
	
	private String username;
	private String firstName;
	private String email;
	private String status;

}
