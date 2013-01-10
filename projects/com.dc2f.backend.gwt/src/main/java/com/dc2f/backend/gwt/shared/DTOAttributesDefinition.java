package com.dc2f.backend.gwt.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Attribute definitions representation as DTO.
 */
public class DTOAttributesDefinition implements Serializable {
	/** **/
	private static final long serialVersionUID = 1L;

	/**
	 * Attribute names available for this node.
	 */
	private String[] attributeNames;
	
	/**
	 * all attribute definitions for each attribute.
	 */
	private Map<String, Node> attributeDefinition = new HashMap<String, Node>();
	
	/**
	 * Required empty constructor.
	 */
	public DTOAttributesDefinition() {
	}
	
	/**
	 * creates a new attribute definition with the given attribute names.
	 * @param attributeNames available attribute names
	 */
	public DTOAttributesDefinition(final String[] attributeNames) {
		this.attributeNames = attributeNames;
	}

	/**
	 * not yet implemented.
	 * @param propertyName attribute name
	 * @return null
	 */
	public Node getAttributeDefinition(final String propertyName) {
		return null;
	}
	
	/**
	 * @return all available attribute names.
	 */
	public String[] getAttributeNames() {
		return attributeNames;
	}

	/**
	 * 
	 * @param attributeName attribute name
	 * @param dtoNode attribute definition node
	 */
	public void setAttributeDefinition(final String attributeName,
			final ContentNode dtoNode) {
		attributeDefinition.put(attributeName, dtoNode);
	}

}
