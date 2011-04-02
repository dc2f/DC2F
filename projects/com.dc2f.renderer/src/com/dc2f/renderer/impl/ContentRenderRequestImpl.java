package com.dc2f.renderer.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dc2f.datastore.ContentRepository;
import com.dc2f.datastore.Node;
import com.dc2f.renderer.ContentRenderRequest;

public class ContentRenderRequestImpl implements ContentRenderRequest {

	private ContentRepository contentRepository;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Node[] nodePath;
	private List<Node> nodeStack;
	private List<Node> nodeContextStack = new LinkedList<Node>();

	public ContentRenderRequestImpl(ContentRepository contentRepository, Node[] nodePath) {
		this.contentRepository = contentRepository;
		this.nodePath = nodePath;
		this.nodeStack = Arrays.asList(nodePath);
	}

	@Override
	public Node getNode() {
		return nodePath[nodePath.length-1];
	}
	
	public Node popFromNodeStack() {
		if (nodeStack.size() < 1) {
			return null;
		}
		return nodeStack.remove(0);
	}

	@Override
	public ContentRepository getContentRepository() {
		return contentRepository;
	}

	@Override
	public Object setAttribute(String key, Object value) {
		return attributes.put(key, value);
	}
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public Node[] getNodesInPath() {
		return nodePath;
	}

	@Override
	public Node popNodeContext() {
		return nodeContextStack.remove(0);
	}

	@Override
	public void pushNodeContext(Node node) {
		nodeContextStack.add(0, node);
	}
	
	public Node getCurrentNodeContext() {
		return nodeContextStack.get(0);
	}
}
