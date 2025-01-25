package com.lcwd.electronics.store.exception;

import lombok.Builder;

@Builder
public class ResourceNoFoundException extends RuntimeException {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


public ResourceNoFoundException () {
		
		super("Resource not found exception");
	}

	
	public ResourceNoFoundException (String massage) {
		
		super(massage);
	}

}
