package com.dc2f.datastore.exception;

public class ClassNotFoundForNodeTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ClassNotFoundForNodeTypeException(String message, Throwable cause) {
		super(message, cause);
	}
}
