package com.dc2f.backend.gwt.client.services;

import java.util.List;

import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * service which can be used to navigate the node hierarchie.
 */
public interface DC2FNavigationService extends RemoteService {
	
	/**
	 * returns the node at the given path.
	 * @param path path of the node which should be returned.
	 * @return a node info object.
	 */
	DTONodeInfo getNodeForPath(String path);

	/**
	 * retrieve all child nodes of a given path.
	 * @param path parent path for which to return all children.
	 * @return list of all nodes directly beneath the given path.
	 */
	List<DTONodeInfo> getNodesForPath(String path);
}
