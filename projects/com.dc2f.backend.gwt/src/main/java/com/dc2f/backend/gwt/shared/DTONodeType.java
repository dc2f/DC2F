package com.dc2f.backend.gwt.shared;

import java.io.Serializable;

public class DTONodeType implements Serializable {
	private static final long serialVersionUID = 1L;
	private String path;
	private DTOAttributesDefinition attributesDefinition;
	
	
	public DTONodeType() {
	}


	public DTONodeType(String path, DTOAttributesDefinition attrDefinition) {
		this.path = path;
		this.attributesDefinition = attrDefinition;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public DTOAttributesDefinition getAttributesDefinition() {
		return attributesDefinition;
	}
	public void setAttributesDefinition(
			DTOAttributesDefinition attributesDefinition) {
		this.attributesDefinition = attributesDefinition;
	}
}
