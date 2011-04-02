package com.dc2f.renderer.impl;

import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.provider.NodeRendererProvider;

public class DefaultRendererProvider implements NodeRendererProvider {

	public NodeRenderer getNodeRenderer(String renderTypeName) {
		if("com.dc2f.renderer.web".equals(renderTypeName)) {
			return new TemplateRenderer();
		}
		return null;
	}

}
