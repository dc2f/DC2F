package com.dc2f.renderer.nodetype.image;

import java.util.logging.Logger;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.nodetype.ContextRendererNodeType;

public class ImageTagRenderer extends BaseNodeType implements
		ContextRendererNodeType {
	private static final Logger logger = Logger.getLogger(ImageTagRenderer.class.getName());

	@Override
	public String renderNode(Node configNode, ContentRenderRequest request, Node context,
			Object value) {
		logger.info("need to render image tag for image {" + value + "}");
		Node img = (Node) value;
		// FIXME some kind of escaping - PLEASE
		return "<img src=\""+request.getURLMapper().getRenderURL(img)+"\" alt=\""+img.get("alt")+"\" /><!-- DEBUG:" + img + " -->";
	}

}
