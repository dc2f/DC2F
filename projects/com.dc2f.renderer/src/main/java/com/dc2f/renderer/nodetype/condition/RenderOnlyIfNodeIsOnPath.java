package com.dc2f.renderer.nodetype.condition;

import java.util.Arrays;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.nodetype.ContextRendererNodeType;

public class RenderOnlyIfNodeIsOnPath extends BaseNodeType implements
		ContextRendererNodeType {

	@Override
	public String renderNode(final Node configNode, final ContentRenderRequest request,
			final Node context, final Object value) {
		ContentRenderRequest rootRequest = request.getRootContentRenderRequest();
		if (Arrays.asList(rootRequest.getNodesInPath()).contains(value)) {
			return (String) configNode.get("inpath_string");
		} else {
			return (String) configNode.get("notinpath_string");
		}
	}

}
