package com.dc2f.backend.client.services;

import java.util.List;

import com.dc2f.backend.shared.Node;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DC2FNavigationService extends RemoteService {
	
	Node getNodeForPath(String Path);

	List<Node> getNodesForPath(String path);
}
