package com.dc2f.datastore;




public interface ContentRepository {

	/**
	 * Returns the node for the given path - The path must be a simple canonical path.
	 * (No filters, relative paths, etc.)
	 */
	Node getNode(String path);
	
	NodeType getNodeType(String path);

	Node getParentNode(Node node);
	
	/* Node[] filterNode() */
	
	/**
	 * close the repository/transaction, if the repository is writable this will also commit the changes
	 */
	void close();
	
}
