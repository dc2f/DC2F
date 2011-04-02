package com.dc2f.datastore;




public interface ContentRepository {

	/**
	 * Returns the node for the given path - The path must be a simple canonical path.
	 * (No filters, relative paths, etc.)
	 */
	Node getNode(String path);

	/**
	 * Returns all nodes in the given path (e.g. /cmsblog/articles/ will return
	 * { Node(/cmsblog), Node(/articles) }
	 */
	Node[] getNodesInPath(String path);
	
	NodeType getNodeType(String path);

	Node getParentNode(Node node);
	
	/* Node[] filterNode() */
	
}
