package com.dc2f.cr;

import java.util.List;

import com.dc2f.core.Node;

public interface ContentRepository {

	Node getNode(String path);
	
	List<Node> getSubNodes(String path);
	List<Node> getSubNodes(Node node);
	
	/* Node[] filterNode() */
	
}
