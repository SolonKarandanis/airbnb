package com.solon.airbnb.user.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersSearchRequestDTO {
	
	private String username;
	private String firstName;
	private String email;
	private String status;

}
