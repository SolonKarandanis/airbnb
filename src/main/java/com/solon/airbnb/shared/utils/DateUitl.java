package com.solon.airbnb.shared.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateUitl {
    private DateUitl(){}

    public static OffsetDateTime convertFromStringToOffsetDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss Z");
        return OffsetDateTime.parse(date,formatter);
    }
}
