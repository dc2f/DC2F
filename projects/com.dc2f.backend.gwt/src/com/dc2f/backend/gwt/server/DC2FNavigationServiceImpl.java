package com.dc2f.backend.gwt.server;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.dc2f.backend.gwt.client.services.DC2FNavigationService;
import com.dc2f.backend.gwt.shared.Node;
import com.dc2f.contentrepository.BranchAccess;
import com.dc2f.contentrepository.CRSession;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.filejson.SimpleJsonContentRepositoryProvider;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DC2FNavigationServiceImpl extends RemoteServiceServlet implements DC2FNavigationService {

	/**
	 * generated unique serialization id
	 */
	private static final long serialVersionUID = 4683885240439796785L;
	
	BranchAccess craccess;

	public DC2FNavigationServiceImpl() {
		//NodeRendererFactory factory = NodeRendererFactory.getInstance();
		//NodeRenderer renderer = factory.getRenderer("com.dc2f.renderer.web");
		File crdir = new File(System.getProperty("crdir", "/Users/bigbear3001/Documents/dc2f/design/example"));
		//ContentRepository cr = ContentRepositoryFactory.getInstance().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)crdir.getAbsolutePath()));
		ContentRepository cr = new SimpleJsonContentRepositoryProvider().getContentRepository("simplejsonfile", Collections.singletonMap("directory", (Object)crdir.getAbsolutePath()));
		CRSession conn = cr.authenticate(null);
		craccess = conn.openTransaction(null);
	}
	
	public List<Node> getNodesForPath(String path) {
		Vector<Node> nodes = new Vector<Node>();
		for(com.dc2f.contentrepository.Node dc2fNode : craccess.getChildren(craccess.getNode(path))) {
			Node node = new Node();
			node.setName(dc2fNode.getName());
			node.setPath(dc2fNode.getPath());
			nodes.add(node);
		}
		return nodes;
	}
	
	public Node getNodeForPath(String path) {
		com.dc2f.contentrepository.Node dc2fNode = craccess.getNode(path);
		Node node = new Node();
		node.setName(dc2fNode.getName());
		node.setPath(dc2fNode.getPath());
		return node;
		
	}

}
