package com.dc2f.renderer.nodetype.template;

import java.net.URI;
import java.util.logging.Logger;

import com.dc2f.datastore.BaseNodeType;
import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.nodetype.ContextRendererNodeType;

public class Context extends BaseNodeType {
	private static final Logger logger = Logger.getLogger(Context.class.getName());
	
	public Object resolveFromContext(Node context, ContentRenderRequest request, String propertyName) {
		Node value = (Node) context.getProperty(propertyName);

		String replacement = "";
		if (value != null) {
			if (value.getNodeType() instanceof ContextRendererNodeType) {
				
				String refContextProperty = (String) value.getProperty("refcontext");
				if (refContextProperty == null) {
					refContextProperty = "renderednode";
				}
				Object renderValue = null;
				String ref = (String) value.getProperty("ref");
				if (refContextProperty.equals("node")) {
					// FIXME we need to do some cool property lookup right here..
					if (ref.startsWith(".@")) {
						renderValue = request.getNode().getProperty(ref.substring(2));
					} else if (ref.equals(".")) {
						renderValue = request.getNode();
					}
				} else if (refContextProperty.equals("request")) {
					renderValue = request.getAttribute(ref);
				} else if (refContextProperty.equals("renderednode")) {
					renderValue = request.getContentRepository().resolveNode(request.getCurrentNodeContext(), ref);
				} else {
					if (renderValue == null) {
						renderValue = value.getProperty("value");
					}
				}
				
				replacement = ((ContextRendererNodeType) value.getNodeType()).renderNode(
						request, context, renderValue);
				if (replacement == null) {
					replacement = "";
				}
			} else {
				replacement = "" + value.getNodeType();
			}
		} else {
			logger.severe("Unable to resolve context variable {" + propertyName
					+ "}");
		}
		return replacement;
	}
}
