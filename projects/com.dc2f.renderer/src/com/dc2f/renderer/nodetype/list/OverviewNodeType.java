package com.dc2f.renderer.nodetype.list;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.impl.ContentRenderRequestImpl;
import com.dc2f.renderer.impl.TemplateRenderer;
import com.dc2f.renderer.nodetype.ContextRendererNodeType;

public class OverviewNodeType extends BaseNodeType implements
		ContextRendererNodeType {

	@Override
	public String renderNode(Node configNode, ContentRenderRequest request,
			Node context, Object value) {
		if (value instanceof Node) {
			Node[] children = request.getContentRepository().getChildren((Node) value);
			StringBuffer buf = new StringBuffer();
			for (Node child : children) {
				ContentRenderRequestImpl req = new ContentRenderRequestImpl(request.getContentRepository(), new Node[]{child});
				buf.append(TemplateRenderer.internalRenderNode(req, null, "com.dc2f.rendertype.web.overview", null));
			}
			return buf.toString();
		}
		
		return "I think we have to render an overview :/ for " + context + " - " + value;
//		return null;
	}

}
