package com.dc2f.renderer.nodetype;

import java.util.ArrayList;
import java.util.List;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.impl.ContentRenderRequestImpl;
import com.dc2f.renderer.impl.WebRenderer;

public class NodeRenderer extends BaseNodeType implements
		ContextRendererNodeType {

	@Override
	public String renderNode(ContentRenderRequest request, Node context, Object value) {
		ContentRepository repository = request.getContentRepository();
		context = request.getCurrentNodeContext();
		if (value instanceof Node) {
			List<Node> nodePath = new ArrayList<Node>();
			// FIXME: This is a stupid way to find the node path :(
			Node node = (Node) value;
			nodePath.add(node);
			while ((node = repository.getParentNode(node)) != null) {
				if (node.equals(context)) {
					break;
				}
				nodePath.add(0, node);
			}
			ContentRenderRequestImpl newRequest = new ContentRenderRequestImpl(request.getContentRepository(), nodePath.toArray(new Node[nodePath.size()]));
			return WebRenderer.internalRenderNode(newRequest, null);
		}
		return "we need to render " + value;
	}

}
