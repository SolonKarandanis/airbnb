package com.solon.airbnb.shared.exception;

public class AccessRightsException extends AirbnbException{

	private static final long serialVersionUID = -8156916897619054330L;
	
	public AccessRightsException() {
        super();
    }

    public AccessRightsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessRightsException(String message) {
        super(message);
    }

}
