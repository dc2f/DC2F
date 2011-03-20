package com.dc2f.datastore;

import com.dc2f.nodetype.NodeType;

public interface Node {
	String getName();
	NodeType getNodeType();
	String getPath();
	Object getProperty(String propertyName);
}
