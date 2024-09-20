package com.solon.airbnb.shared.exception;

public class DocumentException extends RuntimeException {
    private final String errorMessage;

    public DocumentException(String errorMessage){
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
