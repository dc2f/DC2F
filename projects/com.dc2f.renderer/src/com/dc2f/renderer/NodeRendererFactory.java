package com.dc2f.renderer;

import java.util.ServiceLoader;

import com.dc2f.renderer.provider.NodeRendererProvider;

public class NodeRendererFactory {
	private static NodeRendererFactory instance;
	private static ServiceLoader<NodeRendererProvider> providers = ServiceLoader.load(NodeRendererProvider.class);

	private NodeRendererFactory() {
		
	}
	
	public static NodeRendererFactory getInstance() {
		if (instance == null) {
			instance = new NodeRendererFactory();
		}
		return instance;
	}

	public NodeRenderer getRenderer(String renderTypeName) {
		
		// find the first renderer and return it ..
		for (NodeRendererProvider provider: providers) {
			NodeRenderer renderer = provider.getNodeRenderer(renderTypeName);
			if (renderer != null) {
				return renderer;
			}
		}
		return null;
	}
}
