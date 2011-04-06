package com.dc2f.datastore;




/**
 * NodeTypes define the behavior for Nodes (Nodes contain the data).
 * 
 * @author herbert
 */
public interface NodeType {
	public static final Class<? extends NodeType> DEFAULT_NODETYPE = DefaultNodeType.class;
	
	public NodeTypeInfo getNodeTypeInfo();

	public void init(NodeTypeInfo nodeTypeInfo);
	
	public AttributesDefinition getAttributeDefinitions();
}
