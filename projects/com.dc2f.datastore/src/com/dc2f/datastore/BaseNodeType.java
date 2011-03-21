package com.dc2f.datastore;


public abstract class BaseNodeType implements NodeType {

	private NodeTypeInfo nodeTypeInfo;

	public void init(NodeTypeInfo nodeTypeInfo) {
		this.nodeTypeInfo = nodeTypeInfo;
	}
	
	public NodeTypeInfo getNodeTypeInfo() {
		return nodeTypeInfo;
	}
}
