package com.dc2f.datastore.exception;

public class UnknownAttributeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UnknownAttributeException(String reason, Exception cause) {
		super(reason, cause);
	}
}
