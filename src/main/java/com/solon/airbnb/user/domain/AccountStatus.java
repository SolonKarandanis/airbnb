/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.user.domain;

/**
 *
 * @author solon
 */
public enum AccountStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    DELETED("DELETED");
	
	private final String value;
	
	private AccountStatus(String value) {
        this.value = value;
    }
	
	public String getValue() {
        return value;
    }
}
