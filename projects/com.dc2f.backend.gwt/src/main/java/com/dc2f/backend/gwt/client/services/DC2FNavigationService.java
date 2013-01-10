package com.dc2f.backend.gwt.client.services;

import java.util.List;

import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface DC2FNavigationService extends RemoteService {
	
	DTONodeInfo getNodeForPath(String Path);

	List<DTONodeInfo> getNodesForPath(String path);
}
