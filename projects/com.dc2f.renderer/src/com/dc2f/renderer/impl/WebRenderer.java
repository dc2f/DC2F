package com.dc2f.renderer.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Logger;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.nodetype.renderable.RenderableNodeType;
import com.dc2f.nodetype.renderable.template.TemplateNodeType;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.ContentRenderResponse;
import com.dc2f.renderer.NodeRenderer;

public class WebRenderer implements NodeRenderer {
	private static final Logger logger = Logger.getLogger(WebRenderer.class.getName());
	
	private static final String RENDER_TYPE = "com.dc2f.rendertype.web";

	public void renderNode(ContentRenderRequest request,
			ContentRenderResponse response) {
		Node node = request.getNode();
		
		if (node.getNodeType() instanceof RenderableNodeType) {
			RenderableNodeType nodeType = (RenderableNodeType) node.getNodeType();
			Node renderConfig = nodeType.getRenderConfiguration(RENDER_TYPE);
			String template = (String) renderConfig.getProperty("template");
			ContentRepository cr = request.getContentRepository();
			logger.info("We need to load template node {" + template + "}");
			try {
				// FIXME this is just stupid!!!
				String templateNodePath = new URI(nodeType.getNodeTypeInfo().getPath()).resolve(template).toString();
				logger.info("finding ... " + templateNodePath);
				
				Node templateNode = cr.getNode(templateNodePath);
				if (templateNode.getNodeType() instanceof TemplateNodeType) {
					String ret = ((TemplateNodeType) templateNode.getNodeType()).renderTemplate(templateNode);
					response.getWriter().write(ret);
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.severe("Unable to render node which is not renderable :)");
		}
	}

}
