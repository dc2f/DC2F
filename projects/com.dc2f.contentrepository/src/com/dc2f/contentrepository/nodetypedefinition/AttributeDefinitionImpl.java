package com.dc2f.contentrepository.nodetypedefinition;

import com.dc2f.contentrepository.AttributeDefinition;
import com.dc2f.contentrepository.AttributeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;

public class AttributeDefinitionImpl implements AttributeDefinition {
	
	private Node node;

	public AttributeDefinitionImpl(Node node) {
		this.node = node;
	}

	@Override
	public String getName() {
		return node.getName();
	}

	@Override
	public NodeType getNodeType() {
		return node.getNodeType();
	}

	@Override
	public String getPath() {
		return node.getPath();
	}

	@Override
	public Object get(String attributeName) {
		return node.get(attributeName);
	}

	@Override
	public AttributeType getAttributeType() {
		return AttributeType.nameToType((String)node.get("type"));
	}

}
