package com.dc2f.nodetype.renderable;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.datastore.NodeTypeInfo;

public class RenderableNodeType extends BaseNodeType {
	private static final Logger logger = Logger.getLogger(RenderableNodeType.class.getName());
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getRenderConfiguration(String renderType) {
		NodeTypeInfo info = this.getNodeTypeInfo();
		List<Map<String, Object>> config = (List<Map<String, Object>>) info.getProperty("renderconfiguration");
		for (Map<String, Object> map : config) {
			if (renderType.equals(map.get("rendertype"))) {
				return map;
			}
		}
		logger.warning("Unable to find render configuration for type {" + renderType + "} available: {" +config.toString() + "}");
		return null;
	}
}
