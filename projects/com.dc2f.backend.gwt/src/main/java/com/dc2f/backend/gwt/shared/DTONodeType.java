package com.dc2f.backend.gwt.shared;

import java.io.Serializable;

/**
 * Node type information.
 */
public class DTONodeType implements Serializable {
	/** **/
	private static final long serialVersionUID = 1L;
	/**
	 * The path of this node type.
	 */
	private String path;
	/**
	 * attribute definitions for this node type.
	 */
	private DTOAttributesDefinition attributesDefinition;

	/**
	 * Initializes this node type (required for GWT deserialization).
	 */
	public DTONodeType() {
	}

	/**
	 * creates a new node type.
	 * 
	 * @param path type has this path.
	 * @param attrDefinition and this attribute definitions.
	 */
	public DTONodeType(final String path, final DTOAttributesDefinition attrDefinition) {
		this.path = path;
		this.attributesDefinition = attrDefinition;
	}

	/**
	 * @return the path of this node type.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * set the path for this node type.
	 * 
	 * @param path node type is at this path.
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @return all attribute definitions for the attributes of this node type.
	 */
	public DTOAttributesDefinition getAttributesDefinition() {
		return attributesDefinition;
	}

	/**
	 * sets all attribute definitions.
	 * 
	 * @param attributesDefinition all attribute definitions.
	 */
	public void setAttributesDefinition(final DTOAttributesDefinition attributesDefinition) {
		this.attributesDefinition = attributesDefinition;
	}
}
