package com.dc2f.contentrepository.exception;

public class ClassNotFoundForNodeTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClassNotFoundForNodeTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}
