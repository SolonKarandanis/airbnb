/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.user.application.dto;

import java.util.Date;

/**
 *
 * @author solon
 */
@SuppressWarnings("MissingSummary")
public class JwtDTO {

    private String token;
    private Date expires;

    public JwtDTO(String token, Date expires) {
        this.expires = expires;
        this.token = token;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the expires
     */
    public Date getExpires() {
        return expires;
    }

    /**
     * @param expires the expires to set
     */
    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
