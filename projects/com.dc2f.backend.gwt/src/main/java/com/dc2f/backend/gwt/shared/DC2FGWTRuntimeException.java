package com.dc2f.backend.gwt.shared;

/**
 * generic runtime exception within dc2f.
 * @author herbert
 */
public class DC2FGWTRuntimeException extends RuntimeException {
	/** **/
	private static final long serialVersionUID = 1L;

	/**
	 * instantiates a generic exception.
	 * @param message message describing the error.
	 * @param cause cause, might be null
	 */
	public DC2FGWTRuntimeException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
