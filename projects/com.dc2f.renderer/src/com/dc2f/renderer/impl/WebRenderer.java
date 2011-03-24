package com.dc2f.renderer.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.ContentRenderResponse;
import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.nodetype.RenderableNodeType;
import com.dc2f.renderer.nodetype.template.TemplateNodeType;

public class WebRenderer implements NodeRenderer {
	private static final Logger logger = Logger.getLogger(WebRenderer.class.getName());
	
	private static final String RENDER_TYPE = "com.dc2f.rendertype.web";

	public void renderNode(ContentRenderRequest request,
			ContentRenderResponse response) {
		String ret = internalRenderNode(request, response, request.getNode(), null);
		try {
			response.getWriter().write(ret);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while writing result.", e);
		}

	}
	
	protected String internalRenderNode(ContentRenderRequest request, ContentRenderResponse response, Node node, String content) {
		if (node.getNodeType() instanceof RenderableNodeType) {
			RenderableNodeType nodeType = (RenderableNodeType) node.getNodeType();
			Node renderConfig = nodeType.getRenderConfiguration(RENDER_TYPE);
			
			Node templateNode = (Node) renderConfig.getProperty("template");
			if (templateNode.getNodeType() instanceof TemplateNodeType) {
				String ret = ((TemplateNodeType) templateNode.getNodeType()).renderTemplate(request, templateNode);

				Node wrapperNode = (Node) renderConfig.getProperty("wrappertemplate");
				request.setAttribute("content", ret);
				if (wrapperNode == null) {
					// Walk up the tree to find a render configuraiton ..
					Node parentNode = node;
					while ((parentNode = request.getContentRepository()
							.getParentNode(parentNode)) != null) {
						if (parentNode.getNodeType() instanceof RenderableNodeType) {
							ret = this.internalRenderNode(request, response, parentNode, ret);
							return ret;
						}
					}
					
				} else {
					// we assume that this is a template ..
					ret = ((TemplateNodeType) wrapperNode.getNodeType()).renderTemplate(request, wrapperNode);
				}
				return ret;
			}
			
			

		} else {
			logger.severe("Unable to render node which is not renderable :)");
		}
		return null;
	}

}
