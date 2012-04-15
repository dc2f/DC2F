package com.dc2f.renderer.provider;

import com.dc2f.renderer.NodeRenderer;

/**
 * A public interface which can be implemented by provider for renderers.
 * Classes should be registered in a META-INF/services file.
 * 
 * @author herbert
 * @see http://download.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html
 */
public interface NodeRendererProvider {
	/**
	 * Should return a NodeRenderer for the given renderTypeName
	 * 
	 * @param renderTypeName
	 * @return a stateless NodeRenderer
	 */
	public NodeRenderer getNodeRenderer(String renderTypeName);
}
