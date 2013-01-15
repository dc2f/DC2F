package com.dc2f.backend.gwt.shared;

import java.io.Serializable;


/**
 * This is basically a copy of {@link com.dc2f.contentrepository.AttributeType} because i was not
 * able to define it as a GWT module, without converting the whole content repository project into a
 * GWT project(?).
 * 
 * @author herbert
 */
public enum DTOAttributeType implements Serializable {
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
	private DTOAttributeType(final String name) {
		this.name = name;
	}

	/**
	 * returns an enum instance based on a string.
	 * @param name the string name of the attribute type
	 * @return an enum instance for the given attribute name.
	 */
	public static DTOAttributeType nameToType(final String name) {
		for (DTOAttributeType type : DTOAttributeType.values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		throw new DC2FGWTRuntimeException("invalid attribute type {" + name + "}", null);
	}

	/**
	 * @return name.
	 */
	public String getName() {
		return name;
	}
}
