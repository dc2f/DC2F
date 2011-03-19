package com.dc2f.renderer;

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
		return null;
	}
}
