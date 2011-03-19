package com.dc2f.renderer;

import java.util.HashMap;
import java.util.Map;

public class NodeRendererFactory {
	private static NodeRendererFactory instance;

	private NodeRendererFactory() {
		
	}
	
	public static NodeRendererFactory getInstance() {
		if (instance == null) {
			instance = new NodeRendererFactory();
		}
		return instance;
	}

	public NodeRenderer getRenderer(String renderTypeName) {
		if("com.dc2f.renderer.web".equals(renderTypeName)) {
			return new WebRenderer();
		}
		return null;
	}
}
