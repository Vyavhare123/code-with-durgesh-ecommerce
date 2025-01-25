package com.lcwd.electronics.store.exception;


public class NoSuchFileException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public NoSuchFileException() {
		
		super();
	}
	
	
public NoSuchFileException(String massage) {
		
		super(massage);
	}

}
