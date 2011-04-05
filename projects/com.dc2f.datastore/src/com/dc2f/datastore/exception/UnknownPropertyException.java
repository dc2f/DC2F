package com.dc2f.datastore.exception;

public class UnknownPropertyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public UnknownPropertyException(String reason, Exception cause) {
		super(reason, cause);
	}
}
