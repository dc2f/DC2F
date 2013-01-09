package com.dc2f.contentrepository.exception;

import com.dc2f.contentrepository.Node;

/**
 * Exception when fetching an attribute that is not known (has no defintion).
 */
public class UnknownAttributeException extends RuntimeException {
	/**
	 * unique serialization version id (please increment with each
	 * incompatible change).
	 */
	private static final long serialVersionUID = 2L;
	/**
	 * create a new unchecked exception for the given node and attribute name.
	 * @param attributeName - name of the unknown attribute
	 * @param node - node where the attribute definition of the given attribute
	 * was not found.
	 */
	public UnknownAttributeException(final String attributeName,
			final Node node) {
		this(attributeName, node, null);
	}
	
	/**
	 * create a new unchecked exception for the given node and attribute name.
	 * @param attributeName - name of the unknown attribute
	 * @param node - node where the attribute definition of the given attribute
	 * was not found.
	 * @param cause - why the attribute definition wasn't found. 
	 */
	public UnknownAttributeException(final String attributeName,
			final Node node, final Exception cause) {
		super("Unknown attribute {" + attributeName + "} for {"
				+ node.getNodeType() + "}", cause);
	}
}
