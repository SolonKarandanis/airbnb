/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author solon
 */
@SuppressWarnings("MissingSummary")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SubmitCredentialsDTO {

    @NotBlank(message = "{user.username.notNull}")
    @Size(min = 1, max = 50)
    private String username;
    @NotBlank(message = "{user.password.notNull}")
    @Size(min = 4, max = 100)
    private String password;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    
    
}
