package com.dc2f.backend.gwt.shared;

import java.util.HashMap;
import java.util.Map;
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
	private Map<String, Object> attributes = new HashMap<String, Object>();

	/**
	 * when attributes are changed, they are stored here, instead of attributes - this way we know
	 * which have been changed.
	 */
	private Map<String, Object> attributesEdited = new HashMap<String, Object>();

	/**
	 * sets a attribute to the given value.
	 * 
	 * @param attributeName name of the attribute
	 * @param attributeValue value for this attribute.
	 */
	public void set(final String attributeName, final Object attributeValue) {
		Object oldValue = this.get(attributeName);
		if ((oldValue != null && oldValue.equals(attributeValue))
				|| oldValue == null && attributeValue == null) {
			// value did not change.
			return;
		}
		attributesEdited.put(attributeName, attributeValue);
	}

	/**
	 * get the value for the given attribute name.
	 * 
	 * @param attributeName which attribute value to return
	 * @return value for the given attribute name.
	 */
	public Object get(final String attributeName) {
		if (attributesEdited.containsKey(attributeName)) {
			return attributesEdited.get(attributeName);
		}
		return attributes.get(attributeName);
	}

	/**
	 * @return a set of all available attribute names.
	 */
	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}
	
	/**
	 * @return returns all attribute modifications.
	 */
	public Map<String, Object> getModifiedAttributes() {
		return attributesEdited;
	}

}
