package com.dc2f.renderer.nodetype;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.datastore.Node;
import com.dc2f.datastore.NodeTypeInfo;

public class RenderableNodeType extends BaseNodeType {
	private static final Logger logger = Logger.getLogger(RenderableNodeType.class.getName());
	
	/**
	 * 
	 * @param node
	 * @param renderType
	 * @param acceptedVariants (Optional list of variants (the first render configuration is returned which matches one of the variants.).)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Node getRenderConfiguration(Node node, String renderType, String[] acceptedVariants) {
		if (acceptedVariants != null) {
			Arrays.sort(acceptedVariants);
		}
		NodeTypeInfo info = this.getNodeTypeInfo();
		List<Node> config = (List<Node>) info.getProperty("renderconfiguration");
		if (config == null) {
			config = (List<Node>) node.getProperty("renderconfiguration");
		}
		for (Node confignode : config) {
			if (renderType.equals(confignode.getProperty("rendertype"))) {
				String nodeVariant = (String) confignode.getProperty("variant");
				if (nodeVariant == null || Arrays.binarySearch(acceptedVariants, nodeVariant) > 0) {
					return confignode;
				}
			}
		}
		logger.warning("Unable to find render configuration for type {" + renderType + "} available: {" +config.toString() + "}");
		return null;
	}
}
