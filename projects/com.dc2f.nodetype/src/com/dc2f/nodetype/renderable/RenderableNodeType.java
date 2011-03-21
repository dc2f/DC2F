package com.dc2f.nodetype.renderable;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.datastore.Node;
import com.dc2f.datastore.NodeTypeInfo;

public class RenderableNodeType extends BaseNodeType {
	private static final Logger logger = Logger.getLogger(RenderableNodeType.class.getName());
	
	@SuppressWarnings("unchecked")
	public Node getRenderConfiguration(String renderType) {
		NodeTypeInfo info = this.getNodeTypeInfo();
		List<Node> config = (List<Node>) info.getProperty("renderconfiguration");
		for (Node node : config) {
			if (renderType.equals(node.getProperty("rendertype"))) {
				return node;
			}
		}
		logger.warning("Unable to find render configuration for type {" + renderType + "} available: {" +config.toString() + "}");
		return null;
	}
}
