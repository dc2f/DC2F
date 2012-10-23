package com.dc2f.contentrepository;

import com.dc2f.contentrepository.exception.InvalidAttributeTypeException;

public enum AttributeType {
	BOOLEAN("Boolean"),
	STRING("String"),
	INTEGER("Integer"),
	NODE_REFERENCE("NodeReference"),
	NODETYPE_REFERENCE("NodeTypeReference"),
	CLOB("clob"),
	BLOB("blob"),
	NODE("Node"),
	LIST_OF_NODES("ListOfNodes")
	;
	private String name;

	private AttributeType(String name) {
		this.name = name;
	}

	public static AttributeType nameToType(String name) {
		for (AttributeType type : AttributeType.values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		throw new InvalidAttributeTypeException("invalid attribute type {" + name + "}", null);
	}
}
