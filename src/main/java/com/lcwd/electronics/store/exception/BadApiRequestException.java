package com.lcwd.electronics.store.exception;

public class BadApiRequestException extends RuntimeException {
	
	public BadApiRequestException() {
		
		super("Bad Request");
	}

public BadApiRequestException(String massage) {
		
		super(massage);
	}

}
