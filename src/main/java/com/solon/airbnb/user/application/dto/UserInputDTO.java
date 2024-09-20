/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.user.application.dto;

import jakarta.validation.constraints.NotNull;
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
public class UserInputDTO {
    
    @NotNull(message = "{user.username.notNull}")
    @Size(min = 1, max = 25, message = "{user.username.size}")
    private String username;
    
    private String password;
    
    @NotNull(message = "{user.firstName.notNull}")
    @Size(min = 1, max = 50, message = "{user.firstName.size}")
    private String firstName;
    
    @NotNull(message = "{user.lastName.notNull}")
    @Size(min = 1, max = 50, message = "{user.lastName.size}")
    private String lastName;
    
    @NotNull(message = "{user.email.notNull}")
    @Size(min = 1, max = 150, message = "{user.email.size}")
    private String email;
    
    @NotNull(message = "{user.phone.notNull}")
    @Size(min = 1, max = 30, message = "{user.phone.size}")
    private String phone;
    
    @Size(min = 1, max = 30, message = "{user.fax.size}")
    private String fax;
    
    @Size(min = 1, max = 50, message = "{user.firstNameEn.size}")
    private String firstNameEn;
    
    @Size(min = 1, max = 50, message = "{user.lastNameEn.size}")
    private String lastNameEn;
    
    @NotNull(message = "{user.status.notNull}")
    private String status;

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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFirstNameEn() {
		return firstNameEn;
	}

	public void setFirstNameEn(String firstNameEn) {
		this.firstNameEn = firstNameEn;
	}

	public String getLastNameEn() {
		return lastNameEn;
	}

	public void setLastNameEn(String lastNameEn) {
		this.lastNameEn = lastNameEn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



    
    
    
}
