package com.dc2f.renderer.nodetype;

import java.util.List;
import java.util.logging.Logger;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.datastore.Node;
import com.dc2f.datastore.NodeTypeInfo;

public class RenderableNodeType extends BaseNodeType {
	private static final Logger logger = Logger.getLogger(RenderableNodeType.class.getName());
	
	@SuppressWarnings("unchecked")
	public Node getRenderConfiguration(Node node, String renderType) {
		NodeTypeInfo info = this.getNodeTypeInfo();
		List<Node> config = (List<Node>) info.getProperty("renderconfiguration");
		if (config == null) {
			config = (List<Node>) node.getProperty("renderconfiguration");
		}
		for (Node confignode : config) {
			if (renderType.equals(confignode.getProperty("rendertype"))) {
				return confignode;
			}
		}
		logger.warning("Unable to find render configuration for type {" + renderType + "} available: {" +config.toString() + "}");
		return null;
	}
}
