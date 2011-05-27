package com.dc2f.contentrepository;

/**
 * A Node is the base data container for DC2F. It has a name and a unique path.
 * @author herbert
 */
public interface Node {
	/**
	 * The name of the node which is only unique within the parent of this node.
	 */
	String getName();
	
	/**
	 * The type of this node.
	 */
	NodeType getNodeType();

	/**
	 * the unique path to this node (contains the name) - returned normalized (e.g.
	 * /cmsblog/articles/my-first-article) - Starting with a slash, no tailing
	 * slash!
	 */
	String getPath();
	
	
	
	/**
	 * returns an attribute for this node.
	 */
	Object get(String attributeName);
	
}
