package com.dc2f.backend.gwt.shared;

import java.io.Serializable;

/**
 * Node information which is used for navigation (only contains name, path, type, etc. but NO attributes, etc.)
 */
public class DTONodeInfo implements Serializable {

	/**
	 * generated unique serialization version id.
	 */
	private static final long serialVersionUID = -822679286284618507L;

	/**
	 * name of the node.
	 */
	private String name;

	/**
	 * path of the node.
	 */
	private String path;

	/**
	 * type of the node.
	 */
	private DTONodeType nodeType;

	/**
	 * Whether this node contains sub nodes.
	 */
	private boolean hasSubNodes = false;

	/**
	 * empty constructor required for GWT.
	 */
	public DTONodeInfo() {
	}

	/**
	 * creates a new node info DTO.
	 * 
	 * @param name name of the node
	 * @param path path to the node.
	 * @param nodeType type of the current node.
	 */
	public DTONodeInfo(final String name, final String path, final DTONodeType nodeType) {
		this();
		this.name = name;
		this.path = path;
		this.nodeType = nodeType;
	}

	/**
	 * set the name of this node.
	 * @param givenName sets the name of this node.
	 */
	public void setName(final String givenName) {
		name = givenName;
	}

	/**
	 * @return the base name of this node.
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the path of this node.
	 * @param givenPath path for this node.
	 */
	public void setPath(final String givenPath) {
		path = givenPath;
	}

	/**
	 * @return path to this node. (may not be unique?)
	 */
	public String getPath() {
		return path;
	}

	/**
	 * sets the node type.
	 * @param givenNodeType node type information for this node.
	 */
	public void setNodeType(final DTONodeType givenNodeType) {
		nodeType = givenNodeType;
	}

	/**
	 * @return the node type information for this node.
	 */
	public DTONodeType getNodeType() {
		return nodeType;
	}

	/**
	 * FIXME documentation why we need a same method vs. a standard equals method?
	 * @param otherNode node to check if it is the same as this node.
	 * @return <code>true</code> if the other node is the same object as this node. (Can be an older version of it)
	 */
	public boolean same(final DTONodeInfo otherNode) {
		return otherNode != null && getPath().equals(otherNode.getPath());
	}

	/**
	 * @return true if this node is known to have sub nodes.
	 */
	public boolean hasSubNodes() {
		return hasSubNodes;
	}

	/**
	 * set if this node contains subnodes.
	 * @param hasSubNodes has sub nodes?
	 */
	public void setHasSubNodes(final boolean hasSubNodes) {
		this.hasSubNodes = hasSubNodes;
	}

}
