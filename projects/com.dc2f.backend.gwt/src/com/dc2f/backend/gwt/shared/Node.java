package com.dc2f.backend.gwt.shared;

import java.io.Serializable;

public class Node implements Serializable {

	/**
	 * generated unique serialization version id
	 */
	private static final long serialVersionUID = -822679286284618507L;

	String name;
	
	String path;

	private DTONodeType nodeType;
	
	public Node() {
	}
	public Node(String name, String path, DTONodeType nodeType) {
		this();
		this.name = name;
		this.path = path;
		this.nodeType = nodeType;
	}
	
	public void setName(String givenName) {
		name = givenName;
	}
	
	public String getName() {
		return name;
	}

	public void setPath(String givenPath) {
		path = givenPath;
	}

	public String getPath() {
		return path;
	}
	
	public void setNodeType(DTONodeType givenNodeType) {
		nodeType = givenNodeType;
	}
	
	public DTONodeType getNodeType() {
		return nodeType;
	}

}
