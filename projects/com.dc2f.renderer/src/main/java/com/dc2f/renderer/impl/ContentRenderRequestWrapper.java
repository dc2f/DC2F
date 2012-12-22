package com.dc2f.renderer.impl;

import java.util.List;

import com.dc2f.contentrepository.CRAccess;
import com.dc2f.contentrepository.ContentRepository;
import com.dc2f.contentrepository.Node;
import com.dc2f.renderer.ContentRenderRequest;
import com.dc2f.renderer.url.URLMapper;

public class ContentRenderRequestWrapper implements ContentRenderRequest {
	
	private ContentRenderRequest wrappedRequest;
	private Node[] newNodePath;

	public ContentRenderRequestWrapper(ContentRenderRequest wrappedRequest, Node[] newNodePath) {
		this.wrappedRequest = wrappedRequest;
		this.newNodePath = newNodePath;
	}
	

	public Object getAttribute(String key) {
		return wrappedRequest.getAttribute(key);
	}

	public CRAccess getContentRepositoryTransaction() {
		return wrappedRequest.getContentRepositoryTransaction();
	}

	public Node getCurrentNodeContext() {
		return wrappedRequest.getCurrentNodeContext();
	}

	public Node getNode() {
		if (newNodePath != null) {
			return newNodePath[newNodePath.length-1];
		}
		return wrappedRequest.getNode();
	}

	public Node[] getNodesInPath() {
		if (newNodePath != null) {
			return newNodePath;
		}
		return wrappedRequest.getNodesInPath();
	}

	public List<Node> getRenderContextStack() {
		return wrappedRequest.getRenderContextStack();
	}

	public Node popFromNodeStack() {
		return wrappedRequest.popFromNodeStack();
	}

	public Node popNodeContext() {
		return wrappedRequest.popNodeContext();
	}

	public Node popRenderContext() {
		return wrappedRequest.popRenderContext();
	}

	public void pushNodeContext(Node node) {
		wrappedRequest.pushNodeContext(node);
	}

	public void pushRenderContext(Node addToContext) {
		wrappedRequest.pushRenderContext(addToContext);
	}

	public Object setAttribute(String key, Object value) {
		return wrappedRequest.setAttribute(key, value);
	}


	public URLMapper getURLMapper() {
		return wrappedRequest.getURLMapper();
	}


	public ContentRepository getContentRepository() {
		return wrappedRequest.getContentRepository();
	}


	@Override
	public Node getRenderConfiguration(Node node, String renderType,
			String[] acceptedVariants) {
		return wrappedRequest.getRenderConfiguration(node, renderType, acceptedVariants);
	}


	@Override
	public Node getProjectNode() {
		return wrappedRequest.getProjectNode();
	}


	@Override
	public ContentRenderRequest getRootContentRenderRequest() {
		return wrappedRequest.getRootContentRenderRequest();
	}


	@Override
	public ContentRenderRequest getParentContentRenderRequest() {
		return wrappedRequest.getParentContentRenderRequest();
	}

}
