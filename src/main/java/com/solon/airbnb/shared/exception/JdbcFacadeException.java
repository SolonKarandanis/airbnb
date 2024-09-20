/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.shared.exception;

import java.sql.SQLException;

/**
 *
 * @author solon
 */
@SuppressWarnings("MissingSummary")
public class JdbcFacadeException extends SQLException {

    public JdbcFacadeException() {
        super();
    }

    public JdbcFacadeException(String msg) {
        super(msg);
    }
}
