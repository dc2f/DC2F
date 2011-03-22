package com.dc2f.renderer.nodetype;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.renderer.ContentRenderRequest;

public class StringRenderer extends BaseNodeType implements
		ContextRendererNodeType {

	@Override
	public String renderNode(ContentRenderRequest request,
			Object value) {
		return String.valueOf(value);
	}

}
