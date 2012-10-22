package com.dc2f.backend.gwt.shared;

/**
 * Generic interface that all implementations should extend to allow catching all our excpetions at certain points.
 * @author bigbear3001
 *
 */
public class DC2FException extends RuntimeException {

	/**
	 * Create a new DC2FException.
	 * @param message
	 */
	public DC2FException(String message) {
		super(message);
	}
	
	/**
	 * generated unique serialization id.
	 */
	private static final long serialVersionUID = 1394750574694169864L;

}
