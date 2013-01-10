package com.dc2f.backend.gwt.server;

import java.util.List;
import java.util.Vector;

import com.dc2f.backend.gwt.client.services.DC2FNavigationService;
import com.dc2f.backend.gwt.shared.DTONodeType;
import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.NodeType;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DC2FNavigationServiceImpl extends RemoteServiceServlet implements DC2FNavigationService {

	/**
	 * generated unique serialization id
	 */
	private static final long serialVersionUID = 4683885240439796785L;
	
	BranchAccess craccess;

	public DC2FNavigationServiceImpl() {
		craccess = DC2FAccessManager.getAccess();
	}
	
	public List<DTONodeInfo> getNodesForPath(String path) {
		Vector<DTONodeInfo> nodes = new Vector<DTONodeInfo>();
		for(com.dc2f.contentrepository.Node dc2fNode : craccess.getChildren(craccess.getNode(path))) {
			DTONodeInfo node = new DTONodeInfo();
			node.setName(dc2fNode.getName());
			node.setPath(dc2fNode.getPath());
			NodeType type = dc2fNode.getNodeType();
			if (type != null) {
				node.setNodeType(new DTONodeType(type.getNodeTypeInfo().getPath(), null));
			}
			// TODO We should probably improve this (especially performance wise)
			node.setHasSubNodes(craccess.getChildren(dc2fNode).length > 0);
			nodes.add(node);
		}
		return nodes;
	}
	
	public DTONodeInfo getNodeForPath(String path) {
		com.dc2f.contentrepository.Node dc2fNode = craccess.getNode(path);
		DTONodeInfo node = new DTONodeInfo();
		node.setName(dc2fNode.getName());
		node.setPath(dc2fNode.getPath());
		node.setNodeType(new DTONodeType(node.getNodeType().getPath(), null));
		return node;
		
	}

}
