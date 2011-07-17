package com.dc2f.backend.gwt.client.services;

import java.util.List;

import com.dc2f.backend.gwt.shared.ContentNode;
import com.dc2f.backend.gwt.shared.Node;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DC2FContentService extends RemoteService {
	
	ContentNode getNodeForPath(String Path);

	List<Node> getNodesForPath(String path);

	ContentNode saveNode(ContentNode actualNode);
}
