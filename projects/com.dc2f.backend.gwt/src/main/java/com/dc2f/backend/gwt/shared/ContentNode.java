package com.dc2f.backend.gwt.shared;

import java.util.HashMap;
import java.util.Set;


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

}