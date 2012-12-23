package com.dc2f.renderer.nodetype.template;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dc2f.contentrepository.BaseNodeType;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.exception.UnknownAttributeException;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.impl.TemplateRenderer;
import com.dc2f.renderer.nodetype.ContextRendererNodeType;

public class Context extends BaseNodeType {
	private static final Logger logger = Logger.getLogger(Context.class.getName());
	
	public Object resolveFromContext(Node context, ContentRenderRequest request, String propertyName) {
		Node value = (Node) context.get(propertyName);
		if (value == null) {
			List<Node> stack = request.getRenderContextStack();
			for (Iterator<Node> i = stack.iterator() ; i.hasNext() && value == null ; ) {
				Node ctx = i.next();
				if (ctx == null) {
					continue;
//					throw new RuntimeException("Context is null?!");
				}
				value = (Node) ctx.get(propertyName);
			}
		}

		String replacement = "";
		if (value != null) {
			if (value.getNodeType() instanceof ContextRendererNodeType) {
				
				String refContextProperty = (String) value.get("refcontext");
				if (refContextProperty == null) {
					refContextProperty = "renderednode";
				}
				Object renderValue = null;
				String ref = (String) value.get("ref");
				if (refContextProperty.equals("node")) {
					// FIXME we need to do some cool property lookup right here..
					if (ref.startsWith(".@")) {
						renderValue = request.getNode().get(ref.substring(2));
					} else if (ref.equals(".")) {
						renderValue = request.getNode();
					}
				} else if (refContextProperty.equals("request")) {
					renderValue = request.getAttribute(ref);
				} else if (refContextProperty.equals("renderednode")) {
					if (ref.startsWith(".@")) {
						try {
							renderValue = request.getCurrentNodeContext().get(ref.substring(2));
						} catch (UnknownAttributeException e) {
							renderValue = "{ERROR While resolving attribute {" + ref.substring(2) + "}}";
							logger.log(Level.SEVERE, "Error while trying to resolve attribute " + ref.substring(2));
						}
					} else {
						renderValue = request.getContentRepositoryTransaction().resolveNode(request.getCurrentNodeContext(), ref);
					}
				} else if (refContextProperty.equals("context")) {
					renderValue = this.resolveFromContext(context, request, ref);
				} else if (refContextProperty.equals("valuenode")) {
					renderValue = value.get("valuenode");
				} else {
					renderValue = value.get("value");
				}
				
				replacement = ((ContextRendererNodeType) value.getNodeType()).renderNode(
						value, request, context, renderValue);
				if (replacement == null) {
					replacement = "";
				}
			} else {
				replacement = TemplateRenderer.internalRenderNodeOfContent(request, value, value, TemplateRenderer.RENDER_TYPE);
				if (replacement == null) {
					replacement = "" + value.getNodeType();
				}
			}
		} else {
			logger.severe("Unable to resolve context variable {" + propertyName
					+ "}");
		}
		return replacement;
	}
}
