package com.dc2f.renderer.nodetype;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;

public class StringRenderer extends BaseNodeType implements
		ContextRendererNodeType {

	@Override
	public String renderNode(Node configNode, ContentRenderRequest request, Node context,
			Object value) {
		return String.valueOf(value);
	}

}
