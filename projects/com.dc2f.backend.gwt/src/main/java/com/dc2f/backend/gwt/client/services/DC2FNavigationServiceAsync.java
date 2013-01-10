package com.dc2f.backend.gwt.client.services;

import java.util.List;

import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DC2FNavigationServiceAsync {

	void getNodeForPath(String Path, AsyncCallback<DTONodeInfo> callback);

	void getNodesForPath(String path, AsyncCallback<List<DTONodeInfo>> callback);

}
