package com.dc2f.backend.gwt.client.services;

import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * service which allows retrieving as well as editing/saving of nodes.
 */
public interface DC2FContentService extends RemoteService, DC2FNavigationService {
	
	/**
	 * returns an editable node at the given path.
	 * @param path the path of the node which should be edited.
	 * @return returns an editable node.
	 */
	DTOEditableNode getEditableNodeForPath(String path);

	/**
	 * save the given editable node.
	 * @param node node which will be saved.
	 * @return the newly saved node.
	 */
	DTOEditableNode saveNode(DTOEditableNode node);
	
	/**
	 * retrieves the raw source code for the given node.
	 * @param node the node for which to return the source.
	 * @return source of the node.
	 */
	String getSource(DTOEditableNode node);
}
