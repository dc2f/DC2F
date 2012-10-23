package com.dc2f.contentrepository.nodetypedefinition;

import java.util.logging.Logger;

import com.dc2f.contentrepository.AttributeDefinition;
import com.dc2f.contentrepository.AttributeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.exception.InvalidAttributeTypeException;

public class AttributeDefinitionImpl implements AttributeDefinition {
	private static final Logger logger = Logger.getLogger(AttributeDefinitionImpl.class.getName());
	private Node node;

	public AttributeDefinitionImpl(Node node) {
		if (node == null) {
			throw new RuntimeException("AttributeDefinition was instantiated with null node?!");
		}
		this.node = node;
	}

	public String getName() {
		return node.getName();
	}

	public NodeType getNodeType() {
		return node.getNodeType();
	}

	public String getPath() {
		return node.getPath();
	}

	public Object get(String attributeName) {
		return node.get(attributeName);
	}

	public AttributeType getAttributeType() {
		try {
			return AttributeType.nameToType((String)node.get("type"));
		} catch (InvalidAttributeTypeException e) {
			throw new InvalidAttributeTypeException("Invalid part for node {" + getPath() + "}", e);
		}
	}

}
