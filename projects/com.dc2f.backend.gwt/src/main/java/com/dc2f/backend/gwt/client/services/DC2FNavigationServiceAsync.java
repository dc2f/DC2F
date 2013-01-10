package com.dc2f.backend.gwt.client.services;

import java.util.List;

import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * service which can be used to navigate the node hierarchie.
 */
public interface DC2FNavigationServiceAsync {

	/**
	 * returns the node at the given path.
	 * @param path path of the node which should be returned.
	 * @param callback a node info object.
	 */
	void getNodeForPath(String path, AsyncCallback<DTONodeInfo> callback);

	/**
	 * retrieve all child nodes of a given path.
	 * @param path parent path for which to return all children.
	 * @param callback list of all nodes directly beneath the given path.
	 */
	void getNodesForPath(String path, AsyncCallback<List<DTONodeInfo>> callback);

}
