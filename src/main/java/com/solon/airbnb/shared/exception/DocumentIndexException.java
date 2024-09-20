package com.solon.airbnb.shared.exception;

public class DocumentIndexException extends RuntimeException {
    private static final long serialVersionUID = 1L;
	private final String errorMessage;

    public DocumentIndexException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
