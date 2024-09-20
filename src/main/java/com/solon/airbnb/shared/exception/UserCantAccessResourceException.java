package com.solon.airbnb.shared.exception;

public class UserCantAccessResourceException extends AccessRightsException{

	private static final long serialVersionUID = -3431483823312614067L;
	
	public UserCantAccessResourceException() {
    }

    public UserCantAccessResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCantAccessResourceException(String message) {
        super(message);
    }

}
