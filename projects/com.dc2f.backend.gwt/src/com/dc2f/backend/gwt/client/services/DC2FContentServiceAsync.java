package com.dc2f.backend.gwt.client.services;

import java.util.List;

import com.dc2f.backend.gwt.shared.ContentNode;
import com.dc2f.backend.gwt.shared.Node;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DC2FContentServiceAsync {

	void getNodeForPath(String Path, AsyncCallback<ContentNode> callback);

	void getNodesForPath(String path, AsyncCallback<List<Node>> callback);

	void saveNode(ContentNode actualNode, AsyncCallback<ContentNode> callback);

}
