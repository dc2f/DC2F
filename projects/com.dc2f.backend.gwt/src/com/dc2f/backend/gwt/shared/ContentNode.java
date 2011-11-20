package com.dc2f.backend.gwt.shared;

import java.util.Set;

import java.util.HashMap;


public class ContentNode extends Node {

	/**
	 * generated unique serialization version id
	 */
	private static final long serialVersionUID = -770557519293658775L;
	
	private HashMap<String, String> attributes = new HashMap<String, String>();

	public void set(String attributeName, String attributeValue) {
		attributes.put(attributeName, attributeValue);
	}
	
	public String get(String attributeName) {
		return attributes.get(attributeName);
	}
	
	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}

	public String getSource() {
		// TODO get the source of the node
		return "Cannot get source at the moment";
	}

}
