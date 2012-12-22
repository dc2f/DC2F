package com.dc2f.renderer.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.dc2f.contentrepository.CRAccess;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.Node;
import com.dc2f.contentrepository.NodeType;
import com.dc2f.contentrepository.NodeTypeInfo;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.nodetype.Project;
import com.dc2f.renderer.url.URLMapper;

public class ContentRenderRequestImpl implements ContentRenderRequest {
	private static Logger logger = Logger.getLogger(ContentRenderRequestImpl.class.getName());

	private ContentRepository contentRepository;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Node[] nodePath;
	private List<Node> nodeStack;
	private List<Node> nodeContextStack = new LinkedList<Node>();
	public List<Node> renderContextStack = new LinkedList<Node>();
	private URLMapper urlMapper;
	private CRAccess crAccess;
	private Node projectNode;

	private ContentRenderRequest parent;

	public ContentRenderRequestImpl(ContentRepository contentRepository, CRAccess crAccess, Node[] nodePath, URLMapper urlMapper) {
		this.contentRepository = contentRepository;
		this.crAccess = crAccess;
		this.nodePath = nodePath;
		this.nodeStack = Arrays.asList(nodePath);
		this.urlMapper = urlMapper;
	}
	
	public void setParentContentRenderRequest(ContentRenderRequest parent) {
		this.parent = parent;
	}

	public Node getNode() {
		return nodePath[nodePath.length-1];
	}
	
	public Node popFromNodeStack() {
		if (nodeStack.size() < 1) {
			return null;
		}
		return nodeStack.remove(0);
	}

	public CRAccess getContentRepositoryTransaction() {
		return crAccess;
	}

	public Object setAttribute(String key, Object value) {
		return attributes.put(key, value);
	}
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public Node[] getNodesInPath() {
		return nodePath;
	}

	public Node popNodeContext() {
		return nodeContextStack.remove(0);
	}

	public void pushNodeContext(Node node) {
		nodeContextStack.add(0, node);
	}
	
	public Node getCurrentNodeContext() {
		return nodeContextStack.get(0);
	}

	public Node popRenderContext() {
		return renderContextStack.remove(0);
	}

	public void pushRenderContext(Node addToContext) {
		renderContextStack.add(0, addToContext);
	}
	
	/**
	 * returns the render context stack - elements MIGHT be null.
	 */
	public List<Node> getRenderContextStack() {
		return renderContextStack;
	}

	public URLMapper getURLMapper() {
		return urlMapper;
	}

	public ContentRepository getContentRepository() {
		return contentRepository;
	}
	
	public void setProjectNode(Node projectNode) {
		this.projectNode = projectNode;
	}
	
	public Node getProjectNode() {
		if (this.projectNode != null) {
			return this.projectNode;
		}
		Node projectNode = null;
		for (int i = 0 ; i < nodePath.length ; i++) {
			if (nodePath[i].getNodeType() instanceof Project) {
				projectNode = nodePath[i];
				break;
			}
		}
		return projectNode;
	}
	
	public Node getRenderConfiguration(Node node, String renderType, String[] acceptedVariants) {
		if (acceptedVariants != null) {
			Arrays.sort(acceptedVariants);
		}
		// find the project node with the render configuration.
		Node projectNode = getProjectNode();
		if (projectNode == null){ 
			throw new RuntimeException("Unable to find project configuration.");
		}
		@SuppressWarnings("unchecked")
		List<Node> config = (List<Node>) projectNode.get("renderconfiguration");
		
		if (config == null) {
//			config = (List<Node>) node.get("renderconfiguration");
			throw new RuntimeException("project does not contain any render configuration?");
		}
		
		for (Node confignode : config) {
			NodeType targetNodeType = (NodeType) confignode.get("targetnodetype");
			if (targetNodeType != null) {
				if (!node.getNodeType().isSubTypeOf(targetNodeType)) {
					continue;
				}
			}
			// TODO also check target path, url, etc.?
			
			if (renderType.equals(confignode.get("rendertype"))) {
				String nodeVariant = (String) confignode.get("variant");
				if (nodeVariant == null || Arrays.binarySearch(acceptedVariants, nodeVariant) >= 0) {
					return confignode;
				}
			}
		}
		logger.warning("Unable to find render configuration for type {" + renderType + "} available: {" +config.toString() + "}");
		return null;
	}

	@Override
	public ContentRenderRequest getRootContentRenderRequest() {
		if (parent == null) {
			return this;
		}
		return parent.getRootContentRenderRequest();
	}

	@Override
	public ContentRenderRequest getParentContentRenderRequest() {
		return parent;
	}
}
