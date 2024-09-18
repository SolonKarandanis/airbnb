/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solon.airbnb.user.domain.converter;


import com.solon.airbnb.user.domain.AccountStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author solon
 */
@Converter(autoApply = true)
public class AccountStatusConverter implements AttributeConverter<AccountStatus, String>{

    @Override
    public String convertToDatabaseColumn(AccountStatus state) {
        return switch (state) {
            case ACTIVE -> "1";
            case INACTIVE -> "2";
            case DELETED -> "0";
            default -> throw new IllegalArgumentException("AccountState [" + state + "] not supported");
        };
    }

    @Override
    public AccountStatus convertToEntityAttribute(String dbData) {
        return switch (dbData) {
            case "0" -> AccountStatus.DELETED;
            case "1" -> AccountStatus.ACTIVE;
            case "2" -> AccountStatus.INACTIVE;
            default -> throw new IllegalArgumentException("AccountState [" + dbData + "] not supported");
        };
    }
    
}
