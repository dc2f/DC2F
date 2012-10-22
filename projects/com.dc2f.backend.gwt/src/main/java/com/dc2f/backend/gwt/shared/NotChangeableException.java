package com.dc2f.backend.gwt.shared;



/**
 * Is thrown in case a node is not changeable but has to be edited to successfull do the operation (e.g. save changes)
 * @author bigbear3001
 *
 */
public class NotChangeableException extends DC2FException {

	/**
	 * Create a new NotChangeableException.
	 * @param message - error message for this exception
	 */
	public NotChangeableException(String message) {
		super(message);
	}

	/**
	 * unique serialization id
	 */
	private static final long serialVersionUID = 9013933170756069072L;

}
