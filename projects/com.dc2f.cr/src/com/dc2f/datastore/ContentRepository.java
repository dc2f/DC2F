package com.dc2f.datastore;

import java.util.List;


public interface ContentRepository {

	Node getNode(String path);
	
	List<Node> getSubNodes(String path);
	List<Node> getSubNodes(Node node);
	
	/* Node[] filterNode() */
	
}
