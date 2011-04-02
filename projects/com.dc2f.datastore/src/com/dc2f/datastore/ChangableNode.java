package com.dc2f.datastore;

/**
 * Extends the {@link Node} interface to allow changing the properties of the node.
 * @author bigbear3001
 *
 */
public interface ChangableNode extends Node {
	/**
	 * Set the property of the node
	 * @param propertyName - name of the property to set
	 * @param propertyValue - value to set to the propery
	 */
	public void setProperty(String propertyName, Object propertyValue);
}
