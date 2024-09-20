package com.solon.airbnb.shared.exception;

public class TagException extends Exception {

    private final String field;

    public TagException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
