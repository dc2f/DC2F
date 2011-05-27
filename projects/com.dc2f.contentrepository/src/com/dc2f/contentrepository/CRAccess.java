package com.dc2f.contentrepository;

/**
 * interface defining methods for accessing content in a contentrepository.
 * i guess this will only be used as part of the BranchAccess interface actually..
 * @author herbert
 */
public interface CRAccess {

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

	Node resolveNode(Node currentNodeContext, String ref);
	
	/**
	 * Returns all direct children of the current node.
	 */
	Node[] getChildren(Node node);
	
	/* Node[] filterNode() */
	
	/**
	 * close the repository/transaction, if the repository is writable this will also commit the changes
	 */
	void close();
	
	/**
	 * @return <code>true</code> if the repository supports transactions, otherwhise return <code>false</code>.
	 */
	boolean supportsTransaction();
	
	public <T extends CRAdapter> T getAdapter(Class<T> adapterInterface);
}
