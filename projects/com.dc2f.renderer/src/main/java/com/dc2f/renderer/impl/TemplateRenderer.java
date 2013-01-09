package com.dc2f.renderer.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dc2f.contentrepository.CRAccess;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.nodetype.BinaryNodeType;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.ContentRenderResponse;
import com.dc2f.renderer.NodeRenderer;
import com.dc2f.renderer.nodetype.RenderableNodeType;
import com.dc2f.renderer.nodetype.template.TemplateNodeType;

/**
 * TODO: rename to NodeRenderer (or TemplateAwareRenderer ? something like that)
 * 
 * @author herbert
 */
public class TemplateRenderer implements NodeRenderer {
	private static final Logger logger = Logger.getLogger(TemplateRenderer.class.getName());
	
	public static final String RENDER_TYPE = "com.dc2f.rendertype.web";

	public void renderNode(ContentRenderRequest request,
			ContentRenderResponse response) {
		Node node = request.getNode();
		NodeType nodeType = node.getNodeType();
		if (nodeType instanceof BinaryNodeType) {
			BinaryNodeType binary = (BinaryNodeType) nodeType;
			response.setMimeType(binary.getMimeType(node));
			OutputStream outputStream = response.getOutputStream();
			InputStream inputStream = ((BinaryNodeType) nodeType).getInputStream(node);
			// FIXME what happens when inputStream is null?!?! (not found?)
			byte[] buf = new byte[1024];
			int c;
			try {
				while ((c = inputStream.read(buf)) > 0) {
					outputStream.write(buf, 0, c);
				}
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while streaming binary stream.", e);
			}
			return;
		}
		
		String ret = internalRenderNode(request, response, null);
		try {
			response.getWriter().write(ret);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while writing result.", e);
		}

	}

	/**
	 * creates a child render request suitable to render nodes which are the contents of other nodes.
	 * @param request parent render request
	 * @param newRootNode the context from where the subnode should be rendered - if it is null the current render iteration will simply continue.
	 * @param nodeToBeRendered the actual node which should be rendered.
	 * @param renderType
	 * @return
	 */
	public static String internalRenderNodeOfContent(ContentRenderRequest request,
			Node newRootNode, Node nodeToBeRendered, String renderType) {
		CRAccess crAccess = request.getContentRepositoryTransaction();
		List<Node> nodePath = new ArrayList<Node>();
		Node node = nodeToBeRendered;
		
		// FIXME: This is a stupid way to find the node path :(
		nodePath.add(node);
		Node context = request.getCurrentNodeContext();
		if (newRootNode != null) {
			context = newRootNode;
		}
		// if we should continue to render until node, and we are already there.. we are done
		if (node.equals(context)) {
			// except, obviously this is a new render run starting at "newRootNode".
			if (newRootNode == null) {
				// nothing more to render.
				return null;
			}
		} else {
			while ((node = crAccess.getParentNode(node)) != null) {
				if (node.equals(context)) {
					break;
				}
				nodePath.add(0, node);
			}
		}
		if (newRootNode != null) {
			nodePath.add(0, context);
		}

		ContentRenderRequestImpl newRequest = new ContentRenderRequestImpl(request.getContentRepository(), request.getContentRepositoryTransaction(), nodePath.toArray(new Node[nodePath.size()]), request.getURLMapper());
		newRequest.setProjectNode(request.getProjectNode());
		return TemplateRenderer.internalRenderNode(newRequest, null, renderType, null);
	}
	public static String internalRenderNode(ContentRenderRequest request, ContentRenderResponse response, String[] acceptedVariants) {
		return internalRenderNode(request, response, RENDER_TYPE, acceptedVariants);
	}
	public static String internalRenderNode(ContentRenderRequest request, ContentRenderResponse response, String renderType, String[] acceptedVariants) {
		for (Node node : request.getNodesInPath()) {
			Node renderConfig = request.getRenderConfiguration(node, renderType, acceptedVariants);
			if (renderConfig != null) {
				Node templateNode = (Node) renderConfig.get("template");
				Node addToContext = (Node) renderConfig.get("addtocontext");
				if (templateNode.getNodeType() instanceof TemplateNodeType) {
					
					if (node.equals(request.getNode())) {
						// If the node to be rendered is the same as we would be rendering, check if an 'index' property is set.
						Node indexNode = (Node) renderConfig.get("index");
						if (indexNode != null) {
							Node[] newNodePath = Arrays.copyOf(request.getNodesInPath(), request.getNodesInPath().length + 1);
							newNodePath[newNodePath.length-1] = indexNode;
							request = new ContentRenderRequestWrapper(request, newNodePath);
						}
					}
					
					request.pushNodeContext(node);
					request.pushRenderContext(addToContext);
					try {
						return ((TemplateNodeType) templateNode.getNodeType()).renderTemplate(request, templateNode);
					} finally {
						request.popRenderContext();
						request.popNodeContext();
					}
				}
			}
		}
		return null;
	}
	
	/*
	protected String internalRenderNode(ContentRenderRequest request, ContentRenderResponse response, Node node, String content) {
		if (node.getNodeType() instanceof RenderableNodeType) {
			RenderableNodeType nodeType = (RenderableNodeType) node.getNodeType();
			Node renderConfig = nodeType.getRenderConfiguration(RENDER_TYPE);
			
			Node templateNode = (Node) renderConfig.getProperty("template");
			if (templateNode.getNodeType() instanceof TemplateNodeType) {
				String ret = null;
				//String ret = ((TemplateNodeType) templateNode.getNodeType()).renderTemplate(request, templateNode);

				Node wrapperNode = (Node) renderConfig.getProperty("wrappertemplate");
				//request.setAttribute("content", ret);
				if (wrapperNode == null) {
					// Walk up the tree to find a render configuration ..
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
	*/

}
