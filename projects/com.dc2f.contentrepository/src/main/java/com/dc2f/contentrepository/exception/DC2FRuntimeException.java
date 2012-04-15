package com.dc2f.contentrepository.exception;

/**
 * just a lazy way to avoid (smart) exception handling for now ...
 * 
 * @author herbert
 */
public class DC2FRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DC2FRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
