package com.dc2f.backend.gwt.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DTOAttributesDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	private String[] attributeNames;
	private Map<String, Node> attributeDefinition = new HashMap<String, Node>();
	
	public DTOAttributesDefinition() {
	}
	
	public DTOAttributesDefinition(String[] attributeNames) {
		this.attributeNames = attributeNames.clone();
	}

	public Node getAttributeDefinition(String propertyName) {
		return null;
	}
	
	public String[] getAttributeNames() {
		return attributeNames;
	}

	public void setAttributeDefinition(String attributeName,
			ContentNode dtoNode) {
		attributeDefinition.put(attributeName, dtoNode);
	}

}
