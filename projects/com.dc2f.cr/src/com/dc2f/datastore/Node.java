package com.dc2f.datastore;

public interface Node {
	String getName();
	String getPath();
	Object getProperty(String propertyName);
}
