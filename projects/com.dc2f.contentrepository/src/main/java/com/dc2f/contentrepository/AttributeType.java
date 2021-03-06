package com.dc2f.contentrepository;

import com.dc2f.contentrepository.exception.InvalidAttributeTypeException;

/**
 * contains all supported primitive data types.
 * @author herbert
 */
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
	/**
	 * Attribute type for attributes that contain inline nodes. E.g. child attribute in the given json example
	 * <pre>
	 * {
	 * 	"myattribute": "test",
	 * 	"child": {
	 * 		"myattribute": "child.test"
	 * 	}
	 * }
	 * </pre>
	 */
	NODE("Node"),
	
	/**
	 * a list of nodes.
	 */
	LIST_OF_NODES("ListOfNodes");
	
	/**
	 * Name of the attribute type.
	 */
	private String name;

	/**
	 * construct a new attribute type enum.
	 * @param name name of the attribute type as defined in the content repository.
	 */
	private AttributeType(final String name) {
		this.name = name;
	}
	
	/**
	 * @return public name
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns an enum instance based on a string.
	 * @param name the string name of the attribute type
	 * @return an enum instance for the given attribute name.
	 */
	public static AttributeType nameToType(final String name) {
		for (AttributeType type : AttributeType.values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		throw new InvalidAttributeTypeException("invalid attribute type {" + name + "}", null);
	}
}
