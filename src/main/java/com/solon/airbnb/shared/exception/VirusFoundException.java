package com.solon.airbnb.shared.exception;

public class VirusFoundException extends RepException{
	private static final String defaultMessageKey="error.content.viruses.found";

	public VirusFoundException() {
		super(defaultMessageKey);
	}
	
	public VirusFoundException(String messageKey) {
		super(messageKey);
	}
	
	public VirusFoundException(String messageKey,Throwable t) {
		super(messageKey,t);
	}
	
	public VirusFoundException(String messageKey,String[] args) {
		super(messageKey,args);
	}
}
