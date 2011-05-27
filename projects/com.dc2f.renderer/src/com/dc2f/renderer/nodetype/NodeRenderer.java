package com.dc2f.renderer.nodetype;

import java.util.ArrayList;
import java.util.List;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.impl.ContentRenderRequestImpl;
import com.dc2f.renderer.impl.TemplateRenderer;
import com.dc2f.renderer.nodetype.template.HtmlTemplate;

public class NodeRenderer extends BaseNodeType implements
		ContextRendererNodeType {

	@Override
	public String renderNode(Node configNode, ContentRenderRequest request, Node context, Object value) {
		ContentRepository repository = request.getContentRepository();
		context = request.getCurrentNodeContext();
		if (value instanceof Node) {
			Node valueNode = (Node) value;
			if (valueNode.getNodeType() instanceof HtmlTemplate) {
				return ((HtmlTemplate)valueNode.getNodeType()).renderTemplate(request, valueNode);
			}
			
			Node rootNode = (Node) configNode.get("rootNode");
			if (rootNode != null) {
				context = rootNode;
			}
			
			List<Node> nodePath = new ArrayList<Node>();
			// FIXME: This is a stupid way to find the node path :(
			Node node = (Node) value;
			nodePath.add(node);
			if (node.equals(context)) {
				// nothing more to render ..
				return null;
			}
			while ((node = repository.getParentNode(node)) != null) {
				if (node.equals(context)) {
					break;
				}
				nodePath.add(0, node);
			}
			if (rootNode != null) {
				nodePath.add(0, rootNode);
			}
			
			String renderSubtype = (String) configNode.get("renderSubtype");
			String renderType = TemplateRenderer.RENDER_TYPE;
			if (renderSubtype != null) {
				renderType = renderType + "." + renderSubtype;
			}
			
			ContentRenderRequest newRequest = new ContentRenderRequestImpl(request.getContentRepository(), nodePath.toArray(new Node[nodePath.size()]), request.getURLMapper());
			return TemplateRenderer.internalRenderNode(newRequest, null, renderType, null);
		}
		return "we need to render " + value;
	}

}
