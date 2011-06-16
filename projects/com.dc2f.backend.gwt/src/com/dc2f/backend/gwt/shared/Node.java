package com.dc2f.backend.gwt.shared;

import java.io.Serializable;

public class Node implements Serializable {

	/**
	 * generated unique serialization version id
	 */
	private static final long serialVersionUID = -822679286284618507L;

	String name;
	
	String path;
	
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

}
