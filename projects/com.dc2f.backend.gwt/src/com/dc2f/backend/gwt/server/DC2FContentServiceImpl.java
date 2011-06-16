package com.dc2f.backend.gwt.server;

import com.dc2f.backend.gwt.client.services.DC2FContentService;
import com.dc2f.backend.gwt.shared.ContentNode;
import com.dc2f.contentrepository.BranchAccess;

public class DC2FContentServiceImpl extends DC2FNavigationServiceImpl implements DC2FContentService {

	/**
	 * generated unique serialization id
	 */
	private static final long serialVersionUID = 4683885240439796785L;
	
	BranchAccess craccess;

	public DC2FContentServiceImpl() {
		craccess = DC2FAccessManager.getAccess();
	}
	
	public ContentNode getNodeForPath(String path) {
		com.dc2f.contentrepository.Node dc2fNode = craccess.getNode(path);
		ContentNode node = new ContentNode();
		node.setName(dc2fNode.getName());
		node.setPath(dc2fNode.getPath());
		return node;
	}
	
}