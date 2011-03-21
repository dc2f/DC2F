package com.dc2f.datastore;

/**
 * Information about a node type which is defined in the datastore.
 * 
 * @author herbert
 */
public interface NodeTypeInfo {
	public String getName();
	public Object getProperty(String propertyName);
	public String getPath();
}
