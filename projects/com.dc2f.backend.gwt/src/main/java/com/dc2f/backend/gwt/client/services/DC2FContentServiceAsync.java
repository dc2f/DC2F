package com.dc2f.backend.gwt.client.services;

import java.util.List;

import com.dc2f.backend.gwt.shared.DTOEditableNode;
import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DC2FContentServiceAsync {

	void getNodeForPath(String Path, AsyncCallback<DTOEditableNode> callback);

	void getNodesForPath(String path, AsyncCallback<List<DTONodeInfo>> callback);

	void saveNode(DTOEditableNode actualNode, AsyncCallback<DTOEditableNode> callback);

	void getSource(DTOEditableNode actualNode, AsyncCallback<String> callback);

}
