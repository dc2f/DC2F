package com.dc2f.renderer.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dc2f.contentrepository.CRAccess;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.url.URLMapper;

public class ContentRenderRequestImpl implements ContentRenderRequest {

	private ContentRepository contentRepository;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Node[] nodePath;
	private List<Node> nodeStack;
	private List<Node> nodeContextStack = new LinkedList<Node>();
	public List<Node> renderContextStack = new LinkedList<Node>();
	private URLMapper urlMapper;
	private CRAccess crAccess;

	public ContentRenderRequestImpl(ContentRepository contentRepository, CRAccess crAccess, Node[] nodePath, URLMapper urlMapper) {
		this.contentRepository = contentRepository;
		this.crAccess = crAccess;
		this.nodePath = nodePath;
		this.nodeStack = Arrays.asList(nodePath);
		this.urlMapper = urlMapper;
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
	
	public List<Node> getRenderContextStack() {
		return renderContextStack;
	}

	public URLMapper getURLMapper() {
		return urlMapper;
	}

	public ContentRepository getContentRepository() {
		return contentRepository;
	}
}
