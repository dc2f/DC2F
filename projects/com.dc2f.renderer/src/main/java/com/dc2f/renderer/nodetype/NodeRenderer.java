package com.dc2f.renderer.nodetype;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.impl.TemplateRenderer;
import com.dc2f.renderer.nodetype.template.HtmlTemplate;

public class NodeRenderer extends BaseNodeType implements
		ContextRendererNodeType {

	public String renderNode(Node configNode, ContentRenderRequest request, Node context, Object value) {
		if (value instanceof Node) {
			Node valueNode = (Node) value;
			if (valueNode.getNodeType() instanceof HtmlTemplate) {
				return ((HtmlTemplate)valueNode.getNodeType()).renderTemplate(request, valueNode);
			}
			
			Node rootNode = (Node) configNode.get("rootNode");
			Node node = (Node) value;
			
			
			String renderSubtype = (String) configNode.get("renderSubtype");
			String renderType = TemplateRenderer.RENDER_TYPE;
			if (renderSubtype != null) {
				renderType = renderType + "." + renderSubtype;
			}
			
			return TemplateRenderer.internalRenderNodeOfContent(request, rootNode, node, renderType);
		}
		return "we need to render " + value;
	}


}
