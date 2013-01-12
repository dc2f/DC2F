package com.dc2f.backend.gwt.server;

import java.util.ArrayList;
import java.util.List;

import com.dc2f.backend.gwt.client.services.DC2FNavigationService;
import com.dc2f.backend.gwt.shared.DC2FException;
import com.dc2f.backend.gwt.shared.DTONodeInfo;
import com.dc2f.backend.gwt.shared.DTONodeType;
import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of the navigation service.
 */
public class DC2FNavigationServiceImpl extends RemoteServiceServlet implements DC2FNavigationService {

	/**
	 * generated unique serialization id.
	 */
	private static final long serialVersionUID = 4683885240439796785L;

	/**
	 * content repository accessor.
	 */
	private transient BranchAccess craccess;

	/**
	 * create a new service.
	 */
	public DC2FNavigationServiceImpl() {
		craccess = DC2FAccessManager.getAccess();
	}
	
	/**
	 * @return content repository accessor. (will never be null)
	 */
	protected BranchAccess getBranchAccess() {
		if (craccess == null) {
			throw new DC2FException("unable to load content repository accessor.");
		}
		return craccess;
	}

	/**
	 * @param path parent path for the listed children.
	 * @return returns a list of child nodes below the given path.
	 */
	public List<DTONodeInfo> getNodesForPath(final String path) {
		List<DTONodeInfo> nodes = new ArrayList<DTONodeInfo>();
		for (Node dc2fNode : craccess.getChildren(craccess.getNode(path))) {
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

	/**
	 * @param path the path of the node which will be returned.
	 * @return returns the node at the given path.
	 */
	public DTONodeInfo getNodeForPath(final String path) {
		Node dc2fNode = craccess.getNode(path);
		DTONodeInfo node = new DTONodeInfo();
		node.setName(dc2fNode.getName());
		node.setPath(dc2fNode.getPath());
		node.setNodeType(new DTONodeType(node.getNodeType().getPath(), null));
		return node;

	}

}
