package com.dc2f.contentrepository;




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
	
	/**
	 * returns true if the receiver is either equal to the given parentNodeType, or is a sub type of the parent node type.
	 * @param parentNodeType
	 * @return
	 */
	public boolean isSubTypeOf(NodeType parentNodeType);
}
