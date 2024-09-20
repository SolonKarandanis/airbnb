package com.solon.airbnb.user.repository;

import org.springframework.data.jpa.domain.Specification;

import com.solon.airbnb.user.domain.User;

public class UsersSpecs {
	
	public static Specification<User> whereAccountIsActive(){
		return null;
	}

}
