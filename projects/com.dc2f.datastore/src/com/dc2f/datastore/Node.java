package com.dc2f.datastore;


public interface Node {
	String getName();
	NodeType getNodeType();
	String getPath();
	
	
	
	
	Object getProperty(String propertyName);
	
}
