package com.dc2f.contentrepository.exception;

public class UnknownAttributeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UnknownAttributeException(String reason, Exception cause) {
		super(reason, cause);
	}
}
