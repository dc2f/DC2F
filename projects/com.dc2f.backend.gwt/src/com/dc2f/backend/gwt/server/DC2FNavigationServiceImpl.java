package com.dc2f.backend.gwt.server;

import java.util.List;
import java.util.Vector;

import com.dc2f.backend.gwt.client.services.DC2FNavigationService;
import com.dc2f.backend.gwt.shared.Node;
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
	
	public List<Node> getNodesForPath(String path) {
		Vector<Node> nodes = new Vector<Node>();
		for(com.dc2f.contentrepository.Node dc2fNode : craccess.getChildren(craccess.getNode(path))) {
			Node node = new Node();
			node.setName(dc2fNode.getName());
			node.setPath(dc2fNode.getPath());
			NodeType type = dc2fNode.getNodeType();
			if (type != null) {
				node.setNodeType(type.toString());
			}
			nodes.add(node);
		}
		return nodes;
	}
	
	public Node getNodeForPath(String path) {
		com.dc2f.contentrepository.Node dc2fNode = craccess.getNode(path);
		Node node = new Node();
		node.setName(dc2fNode.getName());
		node.setPath(dc2fNode.getPath());
		node.setNodeType(dc2fNode.getNodeType().toString());
		return node;
		
	}

}
