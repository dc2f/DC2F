package com.dc2f.backend.gwt.client.services;

import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * service which allows retrieving as well as editing/saving of nodes.
 */
public interface DC2FContentServiceAsync extends DC2FNavigationServiceAsync {

	/**
	 * returns an editable node at the given path.
	 * @param path the path of the node which should be edited.
	 * @param callback returns an editable node.
	 */
	void getEditableNodeForPath(String path, AsyncCallback<DTOEditableNode> callback);

	/**
	 * save the given editable node.
	 * @param node node which will be saved.
	 * @param callback the newly saved node.
	 */
	void saveNode(DTOEditableNode node, AsyncCallback<DTOEditableNode> callback);

	/**
	 * retrieves the raw source code for the given node.
	 * @param node the node for which to return the source.
	 * @param callback source of the node.
	 */
	void getSource(DTOEditableNode node, AsyncCallback<String> callback);

}
