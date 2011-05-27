package com.dc2f.contentrepository;

/**
 * Information about a node type which is defined in the content repository.
 * 
 * @author herbert
 */
public interface NodeTypeInfo extends Node {
	public String getName();
	public Object get(String propertyName);
	public String[] getAttributeNames();
	public String getPath();
	
	public NodeType getParentNodeType();
}
