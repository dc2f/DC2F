package com.dc2f.contentrepository;

import com.dc2f.contentrepository.exception.InvalidAttributeTypeException;

public enum AttributeType {
	/**
	 * Attribute type for attributes that contain boolean values (true, false).
	 */
	BOOLEAN("Boolean"),
	/**
	 * Attribute type for attributes that contain strings.
	 */
	STRING("String"),
	/**
	 * Attribute type for attributes that contain integers.
	 */
	INTEGER("Integer"),
	/**
	 * Attribute type for attributes that contain another node. This is used for links.
	 */
	NODE_REFERENCE("NodeReference"),
	/**
	 * Attribute type for attrbutes that hold the type information for the node.
	 */
	NODETYPE_REFERENCE("NodeTypeReference"),
	/**
	 * Attribute type for attributes that contain large texts.
	 */
	CLOB("clob"),
	/**
	 * Attribute type for attributes that contain binaries (images and documents).
	 */
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
