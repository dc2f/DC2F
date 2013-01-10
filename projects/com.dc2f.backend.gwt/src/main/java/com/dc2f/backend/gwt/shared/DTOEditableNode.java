package com.dc2f.backend.gwt.shared;

import java.util.HashMap;
import java.util.Set;


/**
 * Node which allows modification of it's attributes.
 */
public class DTOEditableNode extends DTONodeInfo {

	/**
	 * generated unique serialization version id.
	 */
	private static final long serialVersionUID = -770557519293658775L;
	
	/**
	 * all attributes for this node.
	 */
	private HashMap<String, String> attributes = new HashMap<String, String>();

	/**
	 * sets a attribute to the given value.
	 * @param attributeName name of the attribute
	 * @param attributeValue value for this attribute.
	 */
	public void set(final String attributeName, final String attributeValue) {
		attributes.put(attributeName, attributeValue);
	}
	
	/**
	 * get the value for the given attribute name.
	 * @param attributeName which attribute value to return
	 * @return value for the given attribute name.
	 */
	public String get(final String attributeName) {
		return attributes.get(attributeName);
	}
	
	/**
	 * @return a set of all available attribute names.
	 */
	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}

}
