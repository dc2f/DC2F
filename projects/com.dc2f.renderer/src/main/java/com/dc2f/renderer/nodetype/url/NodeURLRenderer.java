package com.dc2f.renderer.nodetype.url;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.nodetype.ContextRendererNodeType;

public class NodeURLRenderer extends BaseNodeType implements
		ContextRendererNodeType {

	public String renderNode(Node configNode, ContentRenderRequest request,
			Node context, Object value) {
		if (value instanceof Node) {
			return request.getURLMapper().getRenderURL((Node) value);
		}
		return "render the URL";
	}

}
