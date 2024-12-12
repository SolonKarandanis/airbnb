package com.solon.airbnb.shared.exception;

public class ServiceUnavailableException extends RepException{
	
	public ServiceUnavailableException() {
		super();
	}
	
	public ServiceUnavailableException(String messageKey) {
		super(messageKey);
	}
	
	public ServiceUnavailableException(String messageKey,Throwable t) {
		super(messageKey,t);
	}
	
	public ServiceUnavailableException(String messageKey,String[] args) {
		super(messageKey,args);
	}

}
