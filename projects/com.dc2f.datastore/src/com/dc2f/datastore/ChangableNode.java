package com.dc2f.datastore;

/**
 * Extends the {@link Node} interface to allow changing the properties of the node.
 * @author bigbear3001
 *
 */
public interface ChangableNode extends Node {
	/**
	 * Set an attribute of the node
	 * @param attributeName - name of the property to set
	 * @param attributeValue - value to set to the propery
	 */
	public void set(String attributeName, Object attributeValue);
}
