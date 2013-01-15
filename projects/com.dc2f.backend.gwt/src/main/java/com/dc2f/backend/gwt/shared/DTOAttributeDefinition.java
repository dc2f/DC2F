package com.dc2f.backend.gwt.shared;

import java.io.Serializable;

/**
 * DTO for simple attribute definition.
 * @author herbert
 */
public class DTOAttributeDefinition implements Serializable {
	/** **/
	private static final long serialVersionUID = 1L;
	
	/**
	 * type of the given attribute.
	 */
	private DTOAttributeType attributeType;
	
	/**
	 * required for gwt deserialization.
	 */
	public DTOAttributeDefinition() {
	}
	
	/**
	 * creates a new DTOAttributeDefinition object.
	 * @param attributeType of the given type.
	 */
	public DTOAttributeDefinition(final DTOAttributeType attributeType) {
		this.attributeType = attributeType;
	}
	
	/**
	 * @return the attribute type
	 */
	public DTOAttributeType getAttributeType() {
		return attributeType;
	}
}
